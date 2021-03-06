package com.hande.hzero.order.api.controller.v1;

import com.hande.hzero.order.api.vo.SoHeaderSearchVO;
import com.hande.hzero.order.api.vo.SoHeaderVO;
import com.hande.hzero.order.config.SwaggerApiConfig;
import com.hande.hzero.order.app.service.SoHeaderService;
import com.hande.hzero.order.domain.entity.SoHeader;
import com.hande.hzero.order.domain.repository.SoHeaderRepository;
import com.itextpdf.text.DocumentException;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 19:27:38
 */
@Api(tags = SwaggerApiConfig.SO_HEADER)
@RestController("soHeaderController.v1")
@RequestMapping("/v1/{organizationId}/so-header")
public class SoHeaderController extends BaseController {


    private SoHeaderService soHeaderService;

    private SoHeaderRepository soHeaderRepository;

    @Autowired
    public SoHeaderController(SoHeaderService soHeaderService, SoHeaderRepository soHeaderRepository){
        this.soHeaderService = soHeaderService;
        this.soHeaderRepository = soHeaderRepository;
    }

    @ApiOperation(value = "分页查询订单头")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<Page<SoHeader>> listSoHeader(@ApiParam(value = "租户ID", required = true)
                                                                 @PathVariable("organizationId") Long organizationId,
                                                          @RequestBody SoHeaderSearchVO params,
                                                          PageRequest pageRequest){
        return Results.success(soHeaderService.listSoHeader(organizationId, params, pageRequest));
    }

    @ApiOperation(value = "根据头ID查询订单头")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<SoHeader> getSoHeaderBySoHeaderId(@ApiParam(value = "租户ID", required = true)
                                                         @PathVariable("organizationId") Long organizationId,
                                                     @RequestParam Long soHeaderId){
        return Results.success(soHeaderRepository.getSoHeaderBySoHeaderId(organizationId, soHeaderId));
    }

    @ApiOperation(value = "更新订单状态")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<Void> updateSoHeaderStatus(@ApiParam(value = "租户ID", required = true)
                                                         @PathVariable("organizationId") Long organizationId,
                                                     @RequestBody SoHeader soHeader,
                                                     @RequestParam String orderStatus){
        soHeaderService.updateSoHeaderStatus(organizationId, soHeader, orderStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "删除订单")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<Void> deleteSoHeaderById(@ApiParam(value = "租户ID", required = true)
                                                       @PathVariable("organizationId") Long organizationId,
                                                   @RequestBody SoHeader soHeader){
        soHeaderService.deleteSoHeaderById(organizationId, soHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("保存订单")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping("/save")
    public ResponseEntity<SoHeaderVO> saveOrder(@ApiParam(value = "租户ID", required = true)
                                                    @PathVariable("organizationId") Long organizationId,
                                                @RequestBody SoHeaderVO soHeaderVO){
        return Results.success(soHeaderService.saveOrder(organizationId, soHeaderVO));
    }

    @ApiOperation("导出订单数据")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/export")
    public ResponseEntity<Void> exportOrders(@ApiParam(value = "租户ID", required = true)
                                 @PathVariable("organizationId") Long organizationId,
                                             @RequestBody SoHeaderSearchVO params,
                                             PageRequest pageRequest,
                                             HttpServletRequest request,
                                             HttpServletResponse response){
        soHeaderService.exportOrders(organizationId, params, pageRequest, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("导入订单数据")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/import")
    public ResponseEntity<Void> importOrders(@ApiParam(value = "租户ID", required = true)
                                                 @PathVariable("organizationId") Long organizationId,
                                             @RequestParam("fileName") MultipartFile file,
                                             HttpServletRequest request) throws IOException {
        soHeaderService.importOrders(organizationId, file, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("单据打印")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/pdf")
    public ResponseEntity<Void> createPDF(@ApiParam(value = "租户ID", required = true)
                                              @PathVariable("organizationId") Long organizationId,
                                          @RequestBody SoHeaderSearchVO params,
                                          HttpServletResponse response) throws IOException, DocumentException {
        soHeaderService.createPDF(organizationId, params, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
