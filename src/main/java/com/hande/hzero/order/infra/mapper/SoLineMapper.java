package com.hande.hzero.order.infra.mapper;

import com.hande.hzero.order.domain.entity.SoLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author denghuaxin
 * @Create on 2020-08-18
 * @Desc
 */
public interface SoLineMapper extends BaseMapper<SoLine> {

    /**
     * 删除订单行
     *
     * @param organizationId
     * @param soHeaderId
     */
    void deleteSoLineByHeaderId(Long organizationId, Long soHeaderId);

    /**
     * 根据头ID，获取订单行
     *
     * @param organizationId
     * @param soHeaderId
     * @return
     */
    List<SoLine> listSoLinesByHeaderId(Long organizationId, Long soHeaderId);

    /**
     * 保存订单（行） -> 新建订单行
     *
     * @param soLine
     */
    void createSoLine(@Param("soLine") SoLine soLine);

    /**
     * 保存订单（行—）->更新订单行
     *
     * @param soLine
     */
    void updateByHeaderId(@Param("soLine") SoLine soLine);

    /**
     * 根据订单行ID获取订单行
     *
     * @param soLineId
     */
    SoLine getBySoLineId(Long soLineId);

    /**
     * 根据订单行ID删除订单行
     *
     * @param soLineId
     */
    void deleteBySoLineId(Long soLineId);
}
