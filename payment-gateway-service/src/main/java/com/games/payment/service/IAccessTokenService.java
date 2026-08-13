package com.games.payment.service;

import com.games.payment.domain.AccessToken;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 访问令牌Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IAccessTokenService extends IService<AccessToken> {

    /**
     * 查询访问令牌列表
     *
     * @param accessToken 访问令牌
     * @return 访问令牌集合
     */
    List<AccessToken> selectAllList(AccessToken accessToken);

    /**
     * 查询列表
     */
    List<AccessToken> queryList(AccessToken accessToken);

}
