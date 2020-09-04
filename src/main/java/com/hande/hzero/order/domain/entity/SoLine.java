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

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
@ApiModel("订单行信息")
@ModifyAudit
@VersionAudit
@Table(name = "hodr_so_line")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoLine extends AuditDomain {

    @Id
    @GeneratedValue
    @ApiModelProperty("主键ID，自增")
    private Long soLineId;
    @ApiModelProperty("订单头ID")
    private Long soHeaderId;
    @ApiModelProperty("行号")
    private Integer lineNumber;
    @ApiModelProperty("产品ID")
    private Long itemId;
    @ApiModelProperty("数量")
    private Double orderQuantity;
    @ApiModelProperty("产品单位")
    private String orderQuantityUom;
    @ApiModelProperty("销售单价")
    private Double unitSellingPrice;
    @ApiModelProperty("备注")
    private String description;
    @ApiModelProperty("附加信息1")
    private String addition1;
    @ApiModelProperty("附加信息2")
    private String addition2;
    @ApiModelProperty("附加信息3")
    private String addition3;
    @ApiModelProperty("附加信息4")
    private String addition4;
    @ApiModelProperty("附加信息5")
    private String addition5;
    @ApiModelProperty("创建人ID")
    private Long createdBy;

    @Transient
    @ApiModelProperty("物料编码")
    private String itemCode;
    @Transient
    @ApiModelProperty("物料描述")
    private String itemDescription;
    @Transient
    @ApiModelProperty("金额")
    private Double lineAmount;



    public Long getSoLineId() {
        return soLineId;
    }

    public void setSoLineId(Long soLineId) {
        this.soLineId = soLineId;
    }

    public Long getSoHeaderId() {
        return soHeaderId;
    }

    public void setSoHeaderId(Long soHeaderId) {
        this.soHeaderId = soHeaderId;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Double getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Double orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderQuantityUom() {
        return orderQuantityUom;
    }

    public void setOrderQuantityUom(String orderQuantityUom) {
        this.orderQuantityUom = orderQuantityUom;
    }

    public Double getUnitSellingPrice() {
        return unitSellingPrice;
    }

    public void setUnitSellingPrice(Double unitSellingPrice) {
        this.unitSellingPrice = unitSellingPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddition1() {
        return addition1;
    }

    public void setAddition1(String addition1) {
        this.addition1 = addition1;
    }

    public String getAddition2() {
        return addition2;
    }

    public void setAddition2(String addition2) {
        this.addition2 = addition2;
    }

    public String getAddition3() {
        return addition3;
    }

    public void setAddition3(String addition3) {
        this.addition3 = addition3;
    }

    public String getAddition4() {
        return addition4;
    }

    public void setAddition4(String addition4) {
        this.addition4 = addition4;
    }

    public String getAddition5() {
        return addition5;
    }

    public void setAddition5(String addition5) {
        this.addition5 = addition5;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getLineAmount() {
        return lineAmount;
    }

    public void setLineAmount(Double lineAmount) {
        this.lineAmount = lineAmount;
    }

    @Override
    public Long getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "SoLine{" +
                "soLineId=" + soLineId +
                ", soHeaderId=" + soHeaderId +
                ", lineNumber=" + lineNumber +
                ", itemId=" + itemId +
                ", orderQuantity=" + orderQuantity +
                ", orderQuantityUom='" + orderQuantityUom + '\'' +
                ", unitSellingPrice=" + unitSellingPrice +
                ", description='" + description + '\'' +
                ", createdBy=" + createdBy +
                ", itemCode='" + itemCode + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", lineAmount=" + lineAmount +
                '}';
    }
}
