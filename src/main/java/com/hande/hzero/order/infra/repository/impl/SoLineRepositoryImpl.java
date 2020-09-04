package com.hande.hzero.order.infra.repository.impl;

import com.hande.hzero.order.domain.entity.SoLine;
import com.hande.hzero.order.domain.repository.SoLineRepository;
import com.hande.hzero.order.infra.mapper.SoLineMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 16:27:04
 */
@Component
public class SoLineRepositoryImpl extends BaseRepositoryImpl<SoLine> implements SoLineRepository {

    private SoLineMapper soLineMapper;

    @Autowired
    public SoLineRepositoryImpl(SoLineMapper soLineMapper) {
        this.soLineMapper = soLineMapper;
    }

    @Override
    public void deleteSoLineByHeaderId(Long organizationId, Long soHeaderId) {
        soLineMapper.deleteSoLineByHeaderId(organizationId, soHeaderId);
    }

    @Override
    public List<SoLine> listSoLInesByHeaderId(Long organizationId, Long soHeaderId) {
        return soLineMapper.listSoLinesByHeaderId(organizationId, soHeaderId);
    }

    @Override
    public void createSoLine(SoLine soLine) {
        soLineMapper.createSoLine(soLine);
    }

    @Override
    public void updateBySoHeaderId(SoLine soLine) {
        soLineMapper.updateByHeaderId(soLine);
    }

    @Override
    public SoLine getBySoLineId(Long soLineId) {
        return soLineMapper.getBySoLineId(soLineId);
    }

    @Override
    public void deleteBySoLineId(Long soLineId) {
        soLineMapper.deleteBySoLineId(soLineId);
    }

    @Override
    public int getMaxLineNumber(Long soHeaderId) {
        return soLineMapper.getMaxLineNumber(soHeaderId);
    }
}
