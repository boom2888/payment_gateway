package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.AccessTokenMapper;
import com.games.payment.domain.AccessToken;
import com.games.payment.service.IAccessTokenService;

import java.util.List;
import java.util.Map;

/**
 * 访问令牌Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class AccessTokenServiceImpl extends ServiceImpl<AccessTokenMapper, AccessToken> implements IAccessTokenService {

    /**
     * 查询访问令牌列表
     *
     * @param accessToken 访问令牌
     * @return 访问令牌
     */
    @Override
    public List<AccessToken> selectAllList(AccessToken accessToken)
    {
        return getBaseMapper().selectAllList(accessToken);
    }


    @Override
    public List<AccessToken> queryList(AccessToken accessToken) {
        LambdaQueryWrapper<AccessToken> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(accessToken.getAccessToken())){
            lqw.eq(AccessToken::getAccessToken ,accessToken.getAccessToken());
        }
        if (StringUtils.isNotBlank(accessToken.getExpiresIn())){
            lqw.eq(AccessToken::getExpiresIn ,accessToken.getExpiresIn());
        }
        if (StringUtils.isNotBlank(accessToken.getVersion())){
            lqw.eq(AccessToken::getVersion ,accessToken.getVersion());
        }
        if (StringUtils.isNotBlank(accessToken.getTokenType())){
            lqw.eq(AccessToken::getTokenType ,accessToken.getTokenType());
        }
        return this.list(lqw);
    }




}
