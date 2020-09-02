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
import java.util.Date;

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
@ApiModel("物料信息")
@ModifyAudit
@VersionAudit
@Table(name = "hodr_item")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item extends AuditDomain {

    private static final String FIELD_ITEM_ID = "itemId";
    private static final String FIELD_ITEM_CODE = "itemCode";
    private static final String FIELD_ITEM_UOM = "itemUom";
    private static final String FIELD_ITEM_DESCRIPTION = "itemDescription";
    private static final String FIELD_SALEABLE_FLAG = "saleableFlag";
    private static final String FIELD_START_ACTIVE_DATE = "startActiveDate";
    private static final String FIELD_END_ACTIVE_DATE = "endActiveDate";
    private static final String FIELD_ENABLED_FLAG = "enabledFlag";

    @Id
    @GeneratedValue
    @ApiModelProperty("表主键，自增")
    private Long itemId;
    @ApiModelProperty("物料编码")
    private String itemCode;
    @ApiModelProperty("物料单位")
    private String itemUom;
    @ApiModelProperty("物料描述")
    private String itemDescription;
    @ApiModelProperty("可销售标识")
    private Integer saleableFlag;
    @ApiModelProperty("生效开始日期")
    private Date startActiveDate;
    @ApiModelProperty("生效结束日期")
    private Date endActiveDate;
    @ApiModelProperty("启用标识")
    private Integer enabledFlag;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemUom() {
        return itemUom;
    }

    public void setItemUom(String itemUom) {
        this.itemUom = itemUom;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getSaleableFlag() {
        return saleableFlag;
    }

    public void setSaleableFlag(Integer saleableFlag) {
        this.saleableFlag = saleableFlag;
    }

    public Date getStartActiveDate() {
        return startActiveDate;
    }

    public void setStartActiveDate(Date startActiveDate) {
        this.startActiveDate = startActiveDate;
    }

    public Date getEndActiveDate() {
        return endActiveDate;
    }

    public void setEndActiveDate(Date endActiveDate) {
        this.endActiveDate = endActiveDate;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

}
