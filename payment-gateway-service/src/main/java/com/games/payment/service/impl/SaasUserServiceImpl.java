package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasUserMapper;
import com.games.payment.domain.SaasUser;
import com.games.payment.service.ISaasUserService;

import java.util.List;
import java.util.Map;

/**
 * SaaS用户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SaasUserServiceImpl extends ServiceImpl<SaasUserMapper, SaasUser> implements ISaasUserService {

    /**
     * 查询SaaS用户列表
     *
     * @param saasUser SaaS用户
     * @return SaaS用户
     */
    @Override
    public List<SaasUser> selectAllList(SaasUser saasUser)
    {
        return getBaseMapper().selectAllList(saasUser);
    }


    @Override
    public List<SaasUser> queryList(SaasUser saasUser) {
        LambdaQueryWrapper<SaasUser> lqw = Wrappers.lambdaQuery();
        if (saasUser.getUserId() != null){
            lqw.eq(SaasUser::getUserId ,saasUser.getUserId());
        }
        if (saasUser.getCorporationId() != null){
            lqw.eq(SaasUser::getCorporationId ,saasUser.getCorporationId());
        }
        if (StringUtils.isNotBlank(saasUser.getIdRef())){
            lqw.eq(SaasUser::getIdRef ,saasUser.getIdRef());
        }
        if (saasUser.getCreatedAt() != null){
            lqw.eq(SaasUser::getCreatedAt ,saasUser.getCreatedAt());
        }
        if (saasUser.getCreatedBy() != null){
            lqw.eq(SaasUser::getCreatedBy ,saasUser.getCreatedBy());
        }
        if (saasUser.getDeletedAt() != null){
            lqw.eq(SaasUser::getDeletedAt ,saasUser.getDeletedAt());
        }
        if (saasUser.getDeletedBy() != null){
            lqw.eq(SaasUser::getDeletedBy ,saasUser.getDeletedBy());
        }
        if (saasUser.getDeleted() != null){
            lqw.eq(SaasUser::getDeleted ,saasUser.getDeleted());
        }
        return this.list(lqw);
    }




}
