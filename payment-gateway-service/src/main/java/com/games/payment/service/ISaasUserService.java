package com.games.payment.service;

import com.games.payment.domain.SaasUser;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * SaaS用户Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISaasUserService extends IService<SaasUser> {

    /**
     * 查询SaaS用户列表
     *
     * @param saasUser SaaS用户
     * @return SaaS用户集合
     */
    List<SaasUser> selectAllList(SaasUser saasUser);

    /**
     * 查询列表
     */
    List<SaasUser> queryList(SaasUser saasUser);

}
