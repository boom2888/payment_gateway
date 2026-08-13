package com.games.payment.service;

import com.games.payment.domain.EmiEnterpriseInfo;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 企业基本信息及出口业务信息Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiEnterpriseInfoService extends IService<EmiEnterpriseInfo> {

    /**
     * 查询企业基本信息及出口业务信息列表
     *
     * @param emiEnterpriseInfo 企业基本信息及出口业务信息
     * @return 企业基本信息及出口业务信息集合
     */
    List<EmiEnterpriseInfo> selectAllList(EmiEnterpriseInfo emiEnterpriseInfo);

    /**
     * 查询列表
     */
    List<EmiEnterpriseInfo> queryList(EmiEnterpriseInfo emiEnterpriseInfo);

    /**
     * 根据 entityId 查询企业信息
     *
     * @param entityId Binderr 实体ID
     * @return 企业信息
     */
    EmiEnterpriseInfo getByEntityId(Long entityId);

    /**
     * 根据 entityId 更新企业状态
     *
     * @param entityId Binderr 实体ID
     * @param status 新状态
     */
    void updateStatusByEntityId(Long entityId, Integer status);

    /**
     * 根据用户ID查询企业信息
     *
     * @param userId 用户ID
     * @return 企业信息
     */
    EmiEnterpriseInfo getByUserId(Long userId);

}
