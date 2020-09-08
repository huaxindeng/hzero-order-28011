package com.hande.hzero.order.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
@ApiModel("用户信息")
@ModifyAudit
@VersionAudit
@Table(name = "hodr_customer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer extends AuditDomain {

    @Id
    @GeneratedValue
    @ApiModelProperty("表主键，自增")
    private Long customerId;

    @NotNull
    @ApiModelProperty("客户编号")
    private String customerNumber;
    @ApiModelProperty("客户姓名")
    private String customerName;
    @ApiModelProperty("公司ID")
    private Long companyId;
    @ApiModelProperty("启用标识")
    private Integer enabledFlag;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
