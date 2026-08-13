package com.games.payment.service;

import com.games.payment.domain.SaasUserCorporation;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * SaaS用户公司Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISaasUserCorporationService extends IService<SaasUserCorporation> {

    /**
     * 查询SaaS用户公司列表
     *
     * @param saasUserCorporation SaaS用户公司
     * @return SaaS用户公司集合
     */
    List<SaasUserCorporation> selectAllList(SaasUserCorporation saasUserCorporation);

    /**
     * 查询列表
     */
    List<SaasUserCorporation> queryList(SaasUserCorporation saasUserCorporation);

}
