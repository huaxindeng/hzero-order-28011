package com.hande.hzero.order.infra.mapper;

import com.hande.hzero.order.domain.entity.Item;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * @Author denghuaxin
 * @Create on 2020-09-04
 * @Desc
 */
public interface ItemMapper extends BaseMapper<Item> {

    List<Item> listItem(String itemCode, String itemDescription, Long organizationId);
}
