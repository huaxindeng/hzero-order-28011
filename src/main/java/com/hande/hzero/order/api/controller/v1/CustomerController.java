package com.hande.hzero.order.api.controller.v1;

import com.hande.hzero.order.config.SwaggerApiConfig;
import com.hande.hzero.order.domain.entity.Customer;
import com.hande.hzero.order.infra.mapper.CustomerMapper;
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
@Api(tags = SwaggerApiConfig.CUSTOMER)
@RestController("customerController.v1")
@RequestMapping("/v1/{organizationId}/customer")
public class CustomerController extends BaseController {

    private CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @ApiOperation(value = "根据客户名称客户编码模糊查询，公司ID不指定默认为-1")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<List<Customer>> listCompany(@ApiParam(value = "租户ID", required = true)
                                             @PathVariable Long organizationId,
                                                      @RequestParam String customerNumber,
                                                      @RequestParam String customerName,
                                                      @RequestParam Long companyId){
        return Results.success(customerMapper.listCustomer(customerNumber, customerName, companyId, organizationId));
    }
}
