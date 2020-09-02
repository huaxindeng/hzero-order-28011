package com.hande.hzero.order.app.service;

import com.hande.hzero.order.api.vo.SoHeaderSearchVO;
import com.hande.hzero.order.api.vo.SoHeaderVO;
import com.hande.hzero.order.domain.entity.SoHeader;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 19:50:41
 */
public interface SoHeaderService {

    /**
     * 根据查询条件，分页查询订单头
     *
     * @param organizationId
     * @param params
     * @param pageRequest
     * @return
     */
    Page<SoHeader> listSoHeader(Long organizationId, SoHeaderSearchVO params, PageRequest pageRequest);

    /**
     * 更新订单状态
     *
     * @param organizationId
     * @param soHeader
     * @param orderStatus
     */
    void updateSoHeaderStatus(Long organizationId, SoHeader soHeader, String orderStatus);

    /**
     * 删除订单头+行
     *
     * @param organizationId
     * @param soHeader
     */
    void deleteSoHeaderById(Long organizationId, SoHeader soHeader);

    /**
     * 保存订单
     *
     * @param organizationId
     * @param soHeaderVO
     * @return
     */
    SoHeaderVO saveOrder(Long organizationId, SoHeaderVO soHeaderVO);

    /**
     * 保存订单->新建订单
     *
     * @param soHeaderVO
     * @param createdRoleId
     * @return
     */
    SoHeaderVO createOrder(SoHeaderVO soHeaderVO, Long createdRoleId);

    /**
     * 保存订单->更新订单
     *
     * @param soHeaderVO
     * @return
     */
    SoHeaderVO updateBySoHeaderVO(SoHeaderVO soHeaderVO);
}
