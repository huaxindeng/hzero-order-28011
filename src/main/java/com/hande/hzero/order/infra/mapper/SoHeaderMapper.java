package com.hande.hzero.order.infra.mapper;

import com.hande.hzero.order.api.vo.SoHeaderSearchVO;
import com.hande.hzero.order.api.vo.SoHeaderVO;
import com.hande.hzero.order.domain.entity.SoHeader;
import io.choerodon.mybatis.common.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
public interface SoHeaderMapper extends BaseMapper<SoHeader> {

    /**
     * 获取订单头列表
     *
     * @param organizationId
     * @param params
     * @return
     */
    List<SoHeader> listSoHeader(@Param("organizationId") Long organizationId,
                                   @Param("params") SoHeaderSearchVO params);

    /**
     * 根据订单头ID获取订单头信息
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
     * 获取当前自增ID
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
    void createSoHeader(@Param("soHeaderVO") SoHeaderVO soHeaderVO);

    /**
     * 保存订单-->更新订单头
     *
     * @param soHeaderVO
     */
    void updateHeaderById(@Param("soHeaderVO") SoHeaderVO soHeaderVO);
}
