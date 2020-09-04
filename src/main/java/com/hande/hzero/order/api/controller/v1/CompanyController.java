package com.hande.hzero.order.api.controller.v1;

import com.hande.hzero.order.config.SwaggerApiConfig;
import com.hande.hzero.order.domain.entity.Company;
import com.hande.hzero.order.infra.mapper.CompanyMapper;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 19:27:38
 */
@Api(tags = SwaggerApiConfig.COMPANY)
@RestController("companyController.v1")
@RequestMapping("/v1/{organizationId}/company")
public class CompanyController extends BaseController {

    private CompanyMapper companyMapper;

    @Autowired
    public CompanyController(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @ApiOperation(value = "根据公司名称公司编码模糊查询")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<List<Company>> listCompany(@ApiParam(value = "租户ID", required = true)
                                             @PathVariable("organizationId") Long organizationId,
                                                    @RequestParam String companyNumber,
                                                    @RequestParam String companyName){
        return Results.success(companyMapper.listCompany(companyNumber, companyName, organizationId));
    }
}
