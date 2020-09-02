package com.hande.hzero.order.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hande.hzero.order.domain.entity.SoLine;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Persistent;

import java.util.Date;
import java.util.List;

/**
 * 订单明细界面订单头信息
 *
 * @author huaxin.deng@hand-china.com 2020-08-21 10:03:21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoHeaderVO {

    @ApiModelProperty("头ID")
    private Long soHeaderId;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("订单日期")
    private Date orderDate;
    @ApiModelProperty("订单状态")
    private String orderStatus;
    @ApiModelProperty("订单总金额")
    private Double orderAmount;

    @ApiModelProperty("订单行集合")
    @Persistent
    private List<SoLine>  soLines;

    @ApiModelProperty("创建人")
    private Long createdBy;

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

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<SoLine> getSoLines() {
        return soLines;
    }

    public void setSoLines(List<SoLine> soLines) {
        this.soLines = soLines;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
