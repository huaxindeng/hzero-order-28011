package com.hande.hzero.order.app.service;

import com.hande.hzero.order.domain.entity.SoLine;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-20 20:37:35
 */
public interface SoLineService {


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
    List<SoLine> listSoLinesByHeaderId(Long organizationId, Long soHeaderId, PageRequest pageRequest);

    /**
     * 业务层调用
     *
     * @param organizationId
     * @param soHeaderId
     * @return
     */
    List<SoLine> listSoLinesByHeaderId(Long organizationId, Long soHeaderId);

    /**
     * 保存订单->保存订单行
     *
     * @param soLine
     * @return
     */
    void saveLine(SoLine soLine);

    /**
     * 保存订单->新建订单行
     *
     * @param soLine
     */
    void createSoLine(SoLine soLine);

    /**
     * 保存订单-->保存订单行
     *
     * @param soLine
     */
    void updateBySoLineId(SoLine soLine);


    /**
     * 根据订单行ID，删除订单行
     *
     * @param organizationId
     * @param soLineId
     */
    void deleteBySoLineId(Long organizationId, Long soLineId);
}
