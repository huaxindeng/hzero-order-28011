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
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
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
        //2.1 获取自增id
        Long autoIncrementId = soHeaderRepository.getAutoIncrementId();
        //2.2 拼接
        StringBuilder orderNumber = new StringBuilder();
        orderNumber.append("SO");
        sdf = new SimpleDateFormat("yyyyMMdd");
        orderNumber.append(sdf.format(soHeaderVO.getOrderDate()));
        orderNumber.append(String.format("%0" + 6 + "d",autoIncrementId));
        LOGGER.info("new order orderNumber:{}", orderNumber);
        soHeaderVO.setOrderNumber(String.valueOf(orderNumber));
        //3.订单状态默认为NEW，不处理
        //4. 组装其他字段，调用接口
        //4.1 新建订单头
        soHeaderVO.setCreatedBy(createdRoleId);
        soHeaderVO.setSoHeaderId(autoIncrementId);
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


}
