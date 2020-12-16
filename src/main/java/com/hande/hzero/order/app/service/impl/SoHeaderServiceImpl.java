package com.hande.hzero.order.app.service.impl;

import com.hande.hzero.order.api.vo.SoHeaderVO;
import com.hande.hzero.order.domain.dto.RoleDTO;
import com.hande.hzero.order.api.vo.SoHeaderSearchVO;
import com.hande.hzero.order.app.service.SoHeaderService;
import com.hande.hzero.order.app.service.SoLineService;
import com.hande.hzero.order.domain.entity.SoHeader;
import com.hande.hzero.order.domain.entity.SoLine;
import com.hande.hzero.order.domain.repository.SoHeaderRepository;
import com.hande.hzero.order.infra.constant.OrderStatusConstants;
import com.hande.hzero.order.infra.feign.RoleFeignClient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 19:56:28
 */
@Service
public class SoHeaderServiceImpl implements SoHeaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoHeaderServiceImpl.class);
    private static final String FIELD_SALE_MANAGER = "SALE_MANAGER_28011";

    private SoHeaderRepository soHeaderRepository;

    private RoleFeignClient roleFeignClient;

    private SoLineService soLineService;

    @Autowired
    public SoHeaderServiceImpl(SoHeaderRepository soHeaderRepository, RoleFeignClient roleFeignClient, SoLineService soLineService) {
        this.soHeaderRepository = soHeaderRepository;
        this.roleFeignClient = roleFeignClient;
        this.soLineService = soLineService;
    }

    @Override
    public Page<SoHeader> listSoHeader(Long organizationId, SoHeaderSearchVO params, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> soHeaderRepository.listSoHeader(organizationId, params));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSoHeaderStatus(Long organizationId, SoHeader soHeader, String orderStatus) {


        //1. 查询该订单是否存在
        SoHeader header = soHeaderRepository.getSoHeaderBySoHeaderId(organizationId, soHeader.getSoHeaderId());
        if(header == null) {
            throw new CommonException("hodr.error.header.not exist");
        }
        Long nowRole= DetailsHelper.getUserDetails().getRoleId();
        LOGGER.info("当前用户信息：{}", DetailsHelper.getUserDetails());
        //2. 判断当前用户是否和单据创建人一致，不一致报错
        if(!nowRole.equals(header.getCreatedBy())) {
            LOGGER.info("当前用户与单据创建人不一致, nowRole:{}, createdBy:{}",nowRole,header.getCreatedBy());
            throw new CommonException("hodr.error.header.createdBy not match nowRole");
        }
        //3. 根据动作状态判断订单当前状态是否符合变更条件
        if(orderStatus.equals(OrderStatusConstants.ORDER_STATUS_SUBMITED)) {
            if(!header.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_NEW) && !header.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_REJECTED)){
                throw new CommonException("hodr.error.header.order.status wrong");
            }
        }
        else if(orderStatus.equals(OrderStatusConstants.ORDER_STATUS_APPROVED)
                || orderStatus.equals(OrderStatusConstants.ORDER_STATUS_REJECTED)){
            if(!header.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_SUBMITED)) {
                LOGGER.info("当前订单状态--非提交状态--");
                throw new CommonException("hodr.error.header.order.status wrong");
            }
            RoleDTO roleVO = roleFeignClient.getRole(organizationId, nowRole);
            if(!roleVO.getCode().equals(FIELD_SALE_MANAGER)){
                LOGGER.info("当前用户角色不足, roleCode:{}",roleVO.getCode());
                throw new CommonException("hodr.error.header.role is not match");
            }
        }
        try {
            soHeaderRepository.updateOrderStatusById(soHeader.getSoHeaderId(), orderStatus);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSoHeaderById(Long organizationId, SoHeader soHeader) {

        soHeader = soHeaderRepository.getSoHeaderBySoHeaderId(organizationId, soHeader.getSoHeaderId());

        //1. 查询订单是否存在
        if(soHeader == null){
            throw new CommonException("error.hodr.header.not exist");
        }
        //2. 判断当前用户是否和单据创建人一致，不一致报错
        Long nowRole= DetailsHelper.getUserDetails().getRoleId();
        if(!nowRole.equals(soHeader.getCreatedBy())) {
            LOGGER.info("当前用户与单据创建人不一致, nowRole:{}, createdBy:{}",nowRole,soHeader.getCreatedBy());
            throw new CommonException("hodr.error.header.createdBy not match nowRole");
        }
        if(!soHeader.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_NEW)
                && !soHeader.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_REJECTED)){
            throw new CommonException("hodr.error.header.delete.order_status is not match {NEW/REJECTED}");
        }
        //3. 删除订单头和订单行
        try {
            soHeaderRepository.deleteSoHeaderById(organizationId, soHeader.getSoHeaderId());
            //判断订单行集合是否为空
            LOGGER.info("soLineService:{}",soLineService);
            LOGGER.info("soHeader.soHeaderId:{}",soHeader.getSoHeaderId());
            if(null != soLineService.listSoLinesByHeaderId(organizationId, soHeader.getSoHeaderId())) {
                soLineService.deleteSoLineByHeaderId(organizationId, soHeader.getSoHeaderId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SoHeaderVO saveOrder(Long organizationId, SoHeaderVO soHeaderVO) {

        //0. 获取当前用户
        Long nowRole= DetailsHelper.getUserDetails().getRoleId();

        try {
            //1. 判断是新增还是更改
            if(soHeaderVO.getSoHeaderId() == null) {
                //1.1 新增调用新增接口，createOrder
                soHeaderVO = createOrder(soHeaderVO, nowRole);
            } else {
                //1.2 更改调用更改接口
                soHeaderVO = updateBySoHeaderVO(soHeaderVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return soHeaderVO;
    }

    /**
     * 保存订单->更改订单
     *
     * @param soHeaderVO
     * @return
     */
    @Override
    public SoHeaderVO updateBySoHeaderVO(SoHeaderVO soHeaderVO) {

        SoHeader updatedHeader = soHeaderRepository.getSoHeaderBySoHeaderId(1L, soHeaderVO.getSoHeaderId());
        //1. 判断当前单据状态为NEW/REJECTED
        String orderStatus = updatedHeader.getOrderStatus();
        if(!orderStatus.equals(OrderStatusConstants.ORDER_STATUS_NEW)
                && !orderStatus.equals(OrderStatusConstants.ORDER_STATUS_REJECTED)) {
            throw new CommonException("hodr.error.update order order_status not match{NEW/REJECTED}");
        }
        //2. 当前创建人为当前用户
        Long nowRole = DetailsHelper.getUserDetails().getRoleId();
        Long createdBy = updatedHeader.getCreatedBy();
        LOGGER.info("updatedHeader.createdBy:{}", createdBy);
        LOGGER.info("nowRole:{}", nowRole);
        if(!createdBy.equals(nowRole)) {
            throw new CommonException("hodr.error.update createdBy not match nowRole");
        }
        //3. 调用接口
        //3.1 更新头
        soHeaderRepository.updateHeaderById(soHeaderVO);
        //3.2 更新行
        if(soHeaderVO.getSoLines() != null){
            for(int i=0; i<soHeaderVO.getSoLines().size(); i++){
                soHeaderVO.getSoLines().get(i).setSoHeaderId(soHeaderVO.getSoHeaderId());
                soLineService.saveLine(soHeaderVO.getSoLines().get(i));
            }
            List<SoLine> lines = soLineService.listSoLinesByHeaderId(1L, soHeaderVO.getSoHeaderId());
            soHeaderVO.setSoLines(lines);
        }
        return soHeaderVO;
    }


    /**
     * 保存订单->新建订单
     *
     * @param soHeaderVO
     * @return
     */
    @Override
    public SoHeaderVO createOrder(SoHeaderVO soHeaderVO, Long createdRoleId) {

        //1. 校验订单日期是否大于2019年
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(soHeaderVO.getOrderDate()));
        if(year <= 2019) {
            throw new CommonException("hodr.error.create order order_date<=2019");
        }
        //2. 生成订单号 order_number SO2020mmdd0000xx
        Long autoIncrementId;
        if(soHeaderVO.getOrderNumber() == null) {
            //2.1 获取自增id
            autoIncrementId = soHeaderRepository.getAutoIncrementId();
            //2.2 拼接
            StringBuilder orderNumber = new StringBuilder();
            orderNumber.append("SO");
            sdf = new SimpleDateFormat("yyyyMMdd");
            orderNumber.append(sdf.format(soHeaderVO.getOrderDate()));
            orderNumber.append(String.format("%0" + 6 + "d",autoIncrementId));
            LOGGER.info("new order orderNumber:{}", orderNumber);
            soHeaderVO.setOrderNumber(String.valueOf(orderNumber));
        }

        //3.订单状态默认为NEW，不处理
        //4. 组装其他字段，调用接口
        //4.1 新建订单头
        soHeaderVO.setCreatedBy(createdRoleId);
//        soHeaderVO.setSoHeaderId(autoIncrementId);
        soHeaderVO.setOrderStatus(OrderStatusConstants.ORDER_STATUS_NEW);
        soHeaderRepository.createSoHeader(soHeaderVO);

        //4.2. 新建订单行
        if(soHeaderVO.getSoLines() != null) {
            for(int i=0; i<soHeaderVO.getSoLines().size();i++){
                //5.1 将头ID传值给订单行
                soHeaderVO.getSoLines().get(i).setSoHeaderId(soHeaderVO.getSoHeaderId());
                soLineService.createSoLine(soHeaderVO.getSoLines().get(i));
            }
            List<SoLine> lines = soLineService.listSoLinesByHeaderId(1L, soHeaderVO.getSoHeaderId());
            soHeaderVO.setSoLines(lines);
        }
        //6. 返回订单
        return soHeaderVO;
    }

    @Override
    public void exportOrders(Long organizationId,
                             SoHeaderSearchVO params,
                             PageRequest pageRequest,
                             HttpServletRequest request,
                             HttpServletResponse response){
        //1. 获取订单头
        Page<SoHeader> soHeaders = listSoHeader(organizationId, params, pageRequest);
        //2. 创建表结构
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("订单数据");

        //设置要导出的文件的名字
        String fileName = "orders-"+ UUID.randomUUID().toString().substring(0,10) + ".xls";
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        //headers表示excel表中第一行的表头
        String[] headers = {
                "销售订单号",
                "公司名称",
                "客户名称",
                "订单日期",
                "订单状态",
                "订单金额",
                "行号",
                "物料编码",
                "物料描述",
                "产品单位",
                "数量",
                "销售单价",
                "行金额",
                "备注",
        };

        HSSFRow row = sheet.createRow(0);

        //在excel表中添加表头
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#.00");
        //3. 循环获取订单行，拼接数据行
        for(SoHeader header : soHeaders) {
            List<SoLine> lines = soLineService.listSoLinesByHeaderId(organizationId, header.getSoHeaderId());
            for(SoLine line : lines) {
                HSSFRow row1 = sheet.createRow(rowNum);

                row1.createCell(0).setCellValue(header.getOrderNumber());
                row1.createCell(1).setCellValue(header.getCompanyName());
                row1.createCell(2).setCellValue(header.getCustomerName());
                row1.createCell(3).setCellValue(sdf.format(header.getOrderDate()));
                switch(header.getOrderStatus()) {
                    case OrderStatusConstants.ORDER_STATUS_NEW :
                        row1.createCell(4).setCellValue("新建");
                        break;
                    case OrderStatusConstants.ORDER_STATUS_SUBMITED:
                        row1.createCell(4).setCellValue("提交");
                        break;
                    case OrderStatusConstants.ORDER_STATUS_APPROVED:
                        row1.createCell(4).setCellValue("审批");
                        break;
                    case OrderStatusConstants.ORDER_STATUS_REJECTED:
                        row1.createCell(4).setCellValue("拒绝");
                        break;
                    default:
                        break;
                }
                row1.createCell(5).setCellValue(header.getOrderAmount());
                row1.createCell(6).setCellValue(line.getLineNumber());
                row1.createCell(7).setCellValue(line.getItemCode());
                row1.createCell(8).setCellValue(line.getItemDescription());
                row1.createCell(9).setCellValue(line.getOrderQuantityUom());
                row1.createCell(10).setCellValue(df.format(line.getOrderQuantity()));
                row1.createCell(11).setCellValue(df.format(line.getUnitSellingPrice()));
                row1.createCell(12).setCellValue(line.getLineAmount());
                row1.createCell(13).setCellValue(line.getDescription());
                rowNum++;
            }
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void importOrders(Long organizationId, MultipartFile file, HttpServletRequest request) throws IOException {
        //1. 校验文件的有效性
        //1.1 文件后缀
        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new CommonException("上传文件格式不正确");
        }
        //2.1 文件字段序列

        //2. 校验数据存在与否
        InputStream is = file.getInputStream();
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        //3. 获取数据，封装到集合中
        Map<String, SoHeader> headerMap = new HashMap<>();
        List<SoHeader> rowHeader ;
        SoLine nowLine;
        Long nowRole = DetailsHelper.getUserDetails().getRoleId();
        for(int i=1; i<sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row == null) {
                break;
            }
            String orderNumber = row.getCell(0).getStringCellValue();
            //3.1 判断订单头是否已经获取过
            if(!headerMap.containsKey(orderNumber)) {
                rowHeader = listSoHeader(organizationId,
                        new SoHeaderSearchVO(orderNumber), new PageRequest(0, 10)).getContent();
                //3.1.2 不存在，先创建订单再创建订单行
                if(rowHeader == null) {
                    SoHeaderVO newHeader = new SoHeaderVO();
                    newHeader.setOrderNumber(orderNumber);
                    newHeader.setCompanyName(row.getCell(1).getStringCellValue());
                    newHeader.setCustomerName(row.getCell(2).getStringCellValue());
                    newHeader.setOrderDate(row.getCell(3).getDateCellValue());
                    switch(row.getCell(4).getStringCellValue()){
                        case "新建" :
                            newHeader.setOrderStatus(OrderStatusConstants.ORDER_STATUS_NEW);
                            break;
                        case "提交" :
                            newHeader.setOrderStatus(OrderStatusConstants.ORDER_STATUS_APPROVED);
                            break;
                        case "审批" :
                            newHeader.setOrderStatus(OrderStatusConstants.ORDER_STATUS_APPROVED);
                            break;
                        case "拒绝" :
                            newHeader.setOrderStatus(OrderStatusConstants.ORDER_STATUS_REJECTED);
                            break;
                        default:
                            break;
                    }
                    newHeader.setCreatedBy(nowRole);
                    createOrder(newHeader, nowRole);
                    rowHeader = listSoHeader(organizationId,
                            new SoHeaderSearchVO(orderNumber), null).getContent();
                }
                headerMap.put(orderNumber, rowHeader.get(0));
            }

            //3.2 创建订单行
            //3.2.1 获取订单头id
            nowLine = new SoLine();
            nowLine.setSoHeaderId(headerMap.get(orderNumber).getSoHeaderId());
            nowLine.setCreatedBy(nowRole);
            nowLine.setLineNumber((int) row.getCell(6).getNumericCellValue());
            nowLine.setItemCode(row.getCell(7).getStringCellValue());
            nowLine.setOrderQuantityUom(row.getCell(9).getStringCellValue());
            nowLine.setOrderQuantity(Double.valueOf(row.getCell(10).getStringCellValue()));
            nowLine.setUnitSellingPrice(Double.valueOf(row.getCell(11).getStringCellValue()));
            nowLine.setDescription(row.getCell(13).getStringCellValue());
            soLineService.createSoLine(nowLine);

        }
        //5. 释放资源
        is.close();
    }

    @Override
    public void createPDF(Long organizationId, SoHeaderSearchVO params, HttpServletResponse response) throws DocumentException, IOException {

        //1. 获取订单头
        Page<SoHeader> soHeaders = listSoHeader(organizationId, params, new PageRequest(0,10));
        SoHeader soHeader = soHeaders.getContent().get(0);
        //2. 获取订单行
        List<SoLine> lines = soLineService.listSoLinesByHeaderId(organizationId, soHeader.getSoHeaderId());
        //3. 生成pdf
        Document document = new Document(PageSize.A4);
        String fileName = "Orders.pdf";
        File file = new File(fileName);
        PdfWriter.getInstance(document, new FileOutputStream(file));

        document.open();
        //中文字体
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font fontLine = new Font(bfChinese, 12,Font.NORMAL);

        Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.RED);
        Chunk chunk = new Chunk("* PDF打印", font);

        document.add(chunk);
        //4. 生成表格1 填充信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PdfPTable headerTable1 = new PdfPTable(6);
        PdfPCell header = new PdfPCell();
        PdfPCell blankCell = new PdfPCell();
        blankCell.setBorderWidth(0);
        addCell(0,"订单编号",headerTable1);
        addCell(1,params.getOrderNumber(),headerTable1);
        addCell(0,"公司名称",headerTable1);
        addCell(1,soHeader.getCompanyName(),headerTable1);
        addCell(0,"客户名称",headerTable1);
        addCell(1,soHeader.getCustomerName(),headerTable1);
        addCell(0,"订单日期",headerTable1);
        addCell(1,sdf.format(soHeader.getOrderDate()),headerTable1);
        addCell(0,"订单金额",headerTable1);
        addCell(1,soHeader.getOrderAmount().toString(),headerTable1);
        addCell(0,"订单状态",headerTable1);
        addCell(1,getOrderStatus(soHeader.getOrderStatus()),headerTable1);

        document.add(headerTable1);


        //2. 生成表格2 填充信息
        PdfPTable lineTable = new PdfPTable(6);
        header.setBorderWidth(0);

        header.setPhrase(new Phrase("主要", fontLine));
        lineTable.addCell(header);
        lineTable.addCell(blankCell);
        lineTable.addCell(blankCell);
        lineTable.addCell(blankCell);
        lineTable.addCell(blankCell);
        lineTable.addCell(blankCell);
        String[] rowStrs = new String[]{
                "物料编码",
                "物料描述",
                "物料单位",
                "数量",
                "单价",
                "金额"
        };
        addRows(lineTable, rowStrs, fontLine);
        for(SoLine line : lines) {
            rowStrs = new String[]{
                    line.getItemCode(),
                    line.getItemDescription(),
                    line.getOrderQuantityUom(),
                    line.getOrderQuantity().toString(),
                    line.getUnitSellingPrice().toString(),
                    line.getLineAmount().toString()
            };
            addRows(lineTable, rowStrs, fontLine);
        }
        document.add(lineTable);

        document.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("utf-8");

        OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(file);
        byte[] bos = new byte[is.available()];
        is.read(bos);
        os.write(bos);

        os.flush();
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            is.close();
            os.close();
        }
    }

    /**
     * 添加行数据
     * @param table
     * @param rowStrs
     * @param fontLine
     */
    private void addRows(PdfPTable table, String[] rowStrs, Font fontLine) {
        for(String str : rowStrs){
            PdfPCell cell = new PdfPCell(new Paragraph(str,fontLine));
            table.addCell(cell);
        }
    }

    /**
     * 增加单元格
     * @param borderWidth
     * @param value
     * @param table
     * @throws IOException
     * @throws DocumentException
     */
    private void addCell(int borderWidth, String value, PdfPTable table) throws IOException, DocumentException {
        //中文字体
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font fontLine = new Font(bfChinese, 12,Font.NORMAL);
        PdfPCell header = new PdfPCell();
        header.setBorderWidth(borderWidth);
        header.setPhrase(new Phrase(value, fontLine));
        table.addCell(header);
    }

    /**
     * 获取订单状态
     * @param orderStatus
     * @return
     */
    private String getOrderStatus(String orderStatus){
        switch (orderStatus){
            case OrderStatusConstants.ORDER_STATUS_NEW:
                return "新建";
            case OrderStatusConstants.ORDER_STATUS_SUBMITED:
                return "提交";
            case OrderStatusConstants.ORDER_STATUS_APPROVED:
                return "审批";
            case OrderStatusConstants.ORDER_STATUS_REJECTED:
                return "拒绝";
            default:
                return null;

        }
    }

    public void testRebase() {
        String rebase = "rebase";
        System.out.println(rebase);
    }

}
