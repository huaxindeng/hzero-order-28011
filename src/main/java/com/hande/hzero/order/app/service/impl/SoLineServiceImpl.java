package com.hande.hzero.order.app.service.impl;

import com.hande.hzero.order.app.service.SoLineService;
import com.hande.hzero.order.domain.entity.SoHeader;
import com.hande.hzero.order.domain.entity.SoLine;
import com.hande.hzero.order.domain.repository.SoHeaderRepository;
import com.hande.hzero.order.domain.repository.SoLineRepository;
import com.hande.hzero.order.infra.constant.OrderStatusConstants;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-20 20:38:06
 */
@Service
public class SoLineServiceImpl implements SoLineService {

    private SoLineRepository soLineRepository;

    private SoHeaderRepository soHeaderRepository;

    @Autowired
    public SoLineServiceImpl(SoLineRepository soLineRepository, SoHeaderRepository soHeaderRepository) {
        this.soLineRepository = soLineRepository;
        this.soHeaderRepository = soHeaderRepository;
    }

    @Override
    public void deleteSoLineByHeaderId(Long organizationId, Long soHeaderId) {
        soLineRepository.deleteSoLineByHeaderId(organizationId, soHeaderId);
    }

    @Override
    public List<SoLine> listSoLinesByHeaderId(Long organizationId, Long soHeaderId, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> soLineRepository.listSoLInesByHeaderId(organizationId, soHeaderId));
    }

    @Override
    public List<SoLine> listSoLinesByHeaderId(Long organizationId, Long soHeaderId) {
        return soLineRepository.listSoLInesByHeaderId(organizationId, soHeaderId);
    }


    @Override
    public void saveLine(SoLine soLine) {
        //1. 判断是新增还是更改
        if(soLine.getSoLineId() == null) {
            //1.1 新增调用新增接口，createOrder
            createSoLine(soLine);
        } else {
            //1.2 更改调用更改接口
            updateBySoLineId(soLine);
        }
    }

    @Override
    public void createSoLine(SoLine soLine) {

        //1. 校验当前单据数据库状态为NEW/REJECTED
        SoHeader header = soHeaderRepository.getSoHeaderBySoHeaderId(1L, soLine.getSoHeaderId());
        String orderStatus = header.getOrderStatus();
        if(!orderStatus.equals(OrderStatusConstants.ORDER_STATUS_NEW)
                && !orderStatus.equals(OrderStatusConstants.ORDER_STATUS_REJECTED)) {
            throw  new CommonException("hodr.error.createSoLine orderStatus not match{NEW/REJECTED}");
        }
        //2. 校验当前单据创建人为当前用户
        Long createdBy = header.getCreatedBy();
        Long nowRoleId = DetailsHelper.getUserDetails().getRoleId();

        if(!createdBy.equals(nowRoleId)) {
            throw new CommonException("hodr.error.createSoLine createdBy not match nowRole");
        }
        soLine.setCreatedBy(nowRoleId);
        //3. 调用接口
        soLineRepository.createSoLine(soLine);

    }

    @Override
    public void updateBySoLineId(SoLine soLine) {
        //1. 校验当前单据数据库状态为NEW/REJECTED
        SoHeader header = soHeaderRepository.getSoHeaderBySoHeaderId(1L, soLine.getSoHeaderId());
        String orderStatus = header.getOrderStatus();
        if(!orderStatus.equals(OrderStatusConstants.ORDER_STATUS_NEW)
                && !orderStatus.equals(OrderStatusConstants.ORDER_STATUS_REJECTED)) {
            throw  new CommonException("hodr.error.createSoLine orderStatus not match{NEW/REJECTED}");
        }
        //2. 校验当前单据创建人为当前用户
        Long createdBy = header.getCreatedBy();
        Long nowRoleId = DetailsHelper.getUserDetails().getRoleId();

        if(!createdBy.equals(nowRoleId)) {
            throw new CommonException("hodr.error.createSoLine createdBy not match nowRole");
        }

        //3. 调用接口
        soLineRepository.updateBySoHeaderId(soLine);
    }

    @Override
    public void deleteBySoLineId(Long organizationId, Long soLineId) {
        //1. 校验当前单据行是否存在，不存在报错
        SoLine soLine = getBySoLineId(soLineId);
        if(soLine == null){
            throw new CommonException("hdor.error.delete.soLine not exist");
        }
        //2. 校验当前用户是否与单据行创建人一致，不一致报错
        Long nowRole = DetailsHelper.getUserDetails().getRoleId();
        if(!nowRole.equals(soLine.getCreatedBy())){
            throw new CommonException("hdor.error.delete.nowRole not match createdBy");
        }
        //3. 校验当前单据数据库状态是否为NEW/REJECTED，否则报错
        //3.1 判断当前单据是否已经失效或不存在
        if(null == soHeaderRepository.getSoHeaderBySoHeaderId(1L, soLine.getSoHeaderId())) {
            throw new CommonException("hdor.error.currentOrder.is disabled or it is not exist");
        }
        SoHeader header = soHeaderRepository.getSoHeaderBySoHeaderId(1L, soLine.getSoHeaderId());
        if(!header.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_NEW)
                && !header.getOrderStatus().equals(OrderStatusConstants.ORDER_STATUS_REJECTED)) {
            throw new CommonException("hodr.error.delete.orderStatus not match {NEW/REJECTED}");
        }
        //4. 调用接口
        soLineRepository.deleteBySoLineId(soLineId);
    }

    /**
     * 根据订单行ID获取订单
     *
     * @param soLineId
     * @return
     */
    private SoLine getBySoLineId(Long soLineId){
        return soLineRepository.getBySoLineId(soLineId);
    }


}
