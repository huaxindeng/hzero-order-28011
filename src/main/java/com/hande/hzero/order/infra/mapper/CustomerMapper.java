package com.hande.hzero.order.infra.mapper;

import com.hande.hzero.order.domain.entity.Customer;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * @Author denghuaxin
 * @Create on 2020-09-04
 * @Desc
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    List<Customer> listCustomer(String customerNumber, String customerName, Long companyId, Long organizationId);
}
