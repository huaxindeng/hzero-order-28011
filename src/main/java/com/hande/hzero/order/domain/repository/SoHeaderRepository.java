package com.hande.hzero.order.domain.repository;

import com.hande.hzero.order.api.vo.SoHeaderSearchVO;
import com.hande.hzero.order.api.vo.SoHeaderVO;
import com.hande.hzero.order.domain.entity.SoHeader;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
public interface SoHeaderRepository extends BaseRepository<SoHeader> {
    /**
     * 分页查询订单头
     *
     * @param organizationId
     * @param params
     * @return
     */
    List<SoHeader> listSoHeader(Long organizationId, SoHeaderSearchVO params);

    /**
     * 根据头ID查询订单头信息
     *
     * @param organizationId
     * @param soHeaderId
     * @return
     */
    SoHeader getSoHeaderBySoHeaderId(Long organizationId, Long soHeaderId);

    /**
     * 更新订单状态
     *
     * @param soHeaderId
     */
    void updateOrderStatusById(Long soHeaderId, String orderStatus);

    /**
     * 删除订单头
     *
     * @param organizationId
     * @param soHeaderId
     */
    void deleteSoHeaderById(Long organizationId, Long soHeaderId);

    /**
     * 获取订单头主键的下一个自增ID
     *
     * @return
     */
    Long getAutoIncrementId();

    /**
     * 保存订单-->新建订单头
     *
     * @param soHeaderVO
     * @return
     */
    void createSoHeader(SoHeaderVO soHeaderVO);

    /**
     * 保存订单->更新订单头
     *
     * @param soHeaderVO
     */
    void updateHeaderById(SoHeaderVO soHeaderVO);
}
