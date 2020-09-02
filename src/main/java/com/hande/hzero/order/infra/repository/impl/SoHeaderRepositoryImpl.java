package com.hande.hzero.order.infra.repository.impl;

import com.hande.hzero.order.api.vo.SoHeaderSearchVO;
import com.hande.hzero.order.api.vo.SoHeaderVO;
import com.hande.hzero.order.domain.entity.SoHeader;
import com.hande.hzero.order.domain.repository.SoHeaderRepository;
import com.hande.hzero.order.infra.mapper.SoHeaderMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 16:26:30
 */
@Component
public class SoHeaderRepositoryImpl extends BaseRepositoryImpl<SoHeader> implements SoHeaderRepository {

    private SoHeaderMapper soHeaderMapper;

    @Autowired
    public SoHeaderRepositoryImpl(SoHeaderMapper soHeaderMapper){
        this.soHeaderMapper = soHeaderMapper;
    }

    @Override
    public List<SoHeader> listSoHeader(Long organizationId, SoHeaderSearchVO params) {
        return soHeaderMapper.listSoHeader(organizationId, params);
    }

    @Override
    public SoHeader getSoHeaderBySoHeaderId(Long organizationId, Long soHeaderId) {
        return soHeaderMapper.getSoHeaderBySoHeaderId(organizationId, soHeaderId);
    }

    @Override
    public void updateOrderStatusById(Long soHeaderId, String orderStatus) {
        soHeaderMapper.updateOrderStatusById(soHeaderId, orderStatus);
    }

    @Override
    public void deleteSoHeaderById(Long organizationId, Long soHeaderId) {
        soHeaderMapper.deleteSoHeaderById(organizationId, soHeaderId);
    }

    @Override
    public Long getAutoIncrementId() {
        return soHeaderMapper.getAutoIncrementId();
    }

    @Override
    public void createSoHeader(SoHeaderVO soHeaderVO) {
        soHeaderMapper.createSoHeader(soHeaderVO);
    }

    @Override
    public void updateHeaderById(SoHeaderVO soHeaderVO) {
        soHeaderMapper.updateHeaderById(soHeaderVO);
    }
}
