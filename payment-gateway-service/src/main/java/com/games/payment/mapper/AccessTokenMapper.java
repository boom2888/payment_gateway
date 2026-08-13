package com.games.payment.mapper;

import com.games.payment.domain.AccessToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 访问令牌Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface AccessTokenMapper extends BaseMapper<AccessToken> {
    /**
     * 查询访问令牌列表
     *
     * @param accessToken 访问令牌
     * @return 访问令牌集合
     */
    List<AccessToken> selectAllList(AccessToken accessToken);

}
