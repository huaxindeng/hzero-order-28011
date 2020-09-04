package com.hande.hzero.order.infra.mapper;

import com.hande.hzero.order.domain.entity.Company;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * @Author denghuaxin
 * @Create on 2020-09-04
 * @Desc
 */
public interface CompanyMapper extends BaseMapper<Company> {

    List<Company> listCompany(String companyNumber, String companyName, Long organizationId);
}
