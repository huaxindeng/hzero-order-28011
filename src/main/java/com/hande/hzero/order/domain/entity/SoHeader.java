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
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
@ApiModel("订单头信息")
@ModifyAudit
@VersionAudit
@Table(name = "hodr_so_header")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoHeader extends AuditDomain {

    private static final String FIELD_SO_HEADER_ID = "soHeaderId";
    private static final String FIELD_ORDER_NUMBER = "orderNumber";
    private static final String FIELD_COMPANY_ID = "companyId";
    private static final String FIELD_ORDER_DATE = "orderDate";
    private static final String FIELD_ORDER_STATUS = "orderStatus";
    private static final String FIELD_CUSTOMER_ID = "customerId";

    @Id
    @GeneratedValue
    @ApiModelProperty("主键，自增")
    private Long soHeaderId;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("公司ID")
    private Long companyId;
    @ApiModelProperty("订单日期")
    private Date orderDate;
    @ApiModelProperty("订单状态")
    private String orderStatus;
    @ApiModelProperty("客户ID")
    private Long customerId;
    @ApiModelProperty("创建人ID")
    private Long createdBy;

    @ApiModelProperty("公司名称")
    @Transient
    private String companyName;
    @ApiModelProperty("客户名称")
    @Transient
    private String customerName;
    @ApiModelProperty("订单总金额")
    @Transient
    private Double orderAmount;

    public Long getSoHeaderId() {
        return soHeaderId;
    }

    public void setSoHeaderId(Long soHeaderId) {
        this.soHeaderId = soHeaderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }
}
