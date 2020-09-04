package com.hande.hzero.order.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单汇总界面订单头信息
 *
 * @author huaxin.deng@hand-china.com 2020-08-18 19:37:42
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoHeaderSearchVO {

    @ApiModelProperty("公司ID")
    private Long companyId;
    @ApiModelProperty("客户ID")
    private Long customerId;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("物料ID")
    private Long itemId;
    @ApiModelProperty("订单状态")
    private String orderStatus;

    public SoHeaderSearchVO() {
    }

    public SoHeaderSearchVO(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public SoHeaderSearchVO(Long companyId, Long customerId, String orderNumber, Long itemId, String orderStatus) {
        this.companyId = companyId;
        this.customerId = customerId;
        this.orderNumber = orderNumber;
        this.itemId = itemId;
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "SoHeaderSearchVO{" +
                "companyId=" + companyId +
                ", customerId=" + customerId +
                ", orderNumber='" + orderNumber + '\'' +
                ", itemId=" + itemId +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
