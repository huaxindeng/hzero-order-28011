package com.hande.hzero.order.api.controller.v1;

import com.hande.hzero.order.app.service.SoLineService;
import com.hande.hzero.order.config.SwaggerApiConfig;
import com.hande.hzero.order.domain.entity.SoLine;
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

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-21 10:27:42
 */
@Api(tags = SwaggerApiConfig.SO_LINE)
@RestController
@RequestMapping("/v1/{organizationId}/so-line")
public class SoLineController extends BaseController {

    private SoLineService soLineService;

    @Autowired
    public SoLineController(SoLineService soLineService) {
        this.soLineService = soLineService;
    }

    //待测试

    @ApiOperation("根据头ID查询订单行")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<List<SoLine>> listBySoHeaderId(@ApiParam(value = "租户ID", required = true)
                                                             @PathVariable("organizationId") Long organizationId,
                                                             @RequestParam Long soHeaderId,
                                                             PageRequest pageRequest){
        return Results.success(soLineService.listSoLinesByHeaderId(organizationId, soHeaderId, pageRequest));
    }
    @ApiOperation("根据行号，删除订单行")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<Void> deleteBySoLineId(@ApiParam(value = "租户ID", required = true)
                                                     @PathVariable("organizationId") Long organizationId,
                                                 @RequestParam Long soLineId){
        soLineService.deleteBySoLineId(organizationId, soLineId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
