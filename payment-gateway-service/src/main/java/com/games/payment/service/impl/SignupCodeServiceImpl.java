package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SignupCodeMapper;
import com.games.payment.domain.SignupCode;
import com.games.payment.service.ISignupCodeService;

import java.util.List;
import java.util.Map;

/**
 * 注册码Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SignupCodeServiceImpl extends ServiceImpl<SignupCodeMapper, SignupCode> implements ISignupCodeService {

    /**
     * 查询注册码列表
     *
     * @param signupCode 注册码
     * @return 注册码
     */
    @Override
    public List<SignupCode> selectAllList(SignupCode signupCode)
    {
        return getBaseMapper().selectAllList(signupCode);
    }


    @Override
    public List<SignupCode> queryList(SignupCode signupCode) {
        LambdaQueryWrapper<SignupCode> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(signupCode.getIdentifier())){
            lqw.eq(SignupCode::getIdentifier ,signupCode.getIdentifier());
        }
        if (signupCode.getSaasUserCorporationId() != null){
            lqw.eq(SignupCode::getSaasUserCorporationId ,signupCode.getSaasUserCorporationId());
        }
        if (StringUtils.isNotBlank(signupCode.getCode())){
            lqw.eq(SignupCode::getCode ,signupCode.getCode());
        }
        if (StringUtils.isNotBlank(signupCode.getPassword())){
            lqw.eq(SignupCode::getPassword ,signupCode.getPassword());
        }
        if (StringUtils.isNotBlank(signupCode.getType())){
            lqw.eq(SignupCode::getType ,signupCode.getType());
        }
        if (signupCode.getExpireAt() != null){
            lqw.eq(SignupCode::getExpireAt ,signupCode.getExpireAt());
        }
        if (signupCode.getCreatedAt() != null){
            lqw.eq(SignupCode::getCreatedAt ,signupCode.getCreatedAt());
        }
        if (signupCode.getCreatedBy() != null){
            lqw.eq(SignupCode::getCreatedBy ,signupCode.getCreatedBy());
        }
        if (signupCode.getDeletedAt() != null){
            lqw.eq(SignupCode::getDeletedAt ,signupCode.getDeletedAt());
        }
        if (signupCode.getDeletedBy() != null){
            lqw.eq(SignupCode::getDeletedBy ,signupCode.getDeletedBy());
        }
        if (signupCode.getDeleted() != null){
            lqw.eq(SignupCode::getDeleted ,signupCode.getDeleted());
        }
        if (signupCode.getMerchantId() != null){
            lqw.eq(SignupCode::getMerchantId ,signupCode.getMerchantId());
        }
        if (StringUtils.isNotBlank(signupCode.getReferralCode())){
            lqw.eq(SignupCode::getReferralCode ,signupCode.getReferralCode());
        }
        return this.list(lqw);
    }




}
