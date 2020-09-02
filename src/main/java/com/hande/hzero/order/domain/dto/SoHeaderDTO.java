package com.hande.hzero.order.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 订单汇总查询页面，订单头结果展示信息
 *
 * @author huaxin.deng@hand-china.com 2020-08-18 19:32:57
 */
public class SoHeaderDTO {

    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("客户名称")
    private String consumerName;
    @ApiModelProperty("订单日期")
    private Date orderDate;
    @ApiModelProperty("订单状态")
    private String orderStatus;
    @ApiModelProperty("订单总金额")
    private Double orderAmount;

    @Override
    public String toString() {
        return "SoHeaderVO{" +
                "orderNumber='" + orderNumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", orderDate=" + orderDate +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderAmount=" + orderAmount +
                '}';
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

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
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
}
