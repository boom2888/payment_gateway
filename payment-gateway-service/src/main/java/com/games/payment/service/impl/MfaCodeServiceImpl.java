package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.MfaCodeMapper;
import com.games.payment.domain.MfaCode;
import com.games.payment.service.IMfaCodeService;

import java.util.List;
import java.util.Map;

/**
 * MFA代码Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class MfaCodeServiceImpl extends ServiceImpl<MfaCodeMapper, MfaCode> implements IMfaCodeService {

    /**
     * 查询MFA代码列表
     *
     * @param mfaCode MFA代码
     * @return MFA代码
     */
    @Override
    public List<MfaCode> selectAllList(MfaCode mfaCode)
    {
        return getBaseMapper().selectAllList(mfaCode);
    }


    @Override
    public List<MfaCode> queryList(MfaCode mfaCode) {
        LambdaQueryWrapper<MfaCode> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(mfaCode.getCode())){
            lqw.eq(MfaCode::getCode ,mfaCode.getCode());
        }
        if (StringUtils.isNotBlank(mfaCode.getOtpAscii())){
            lqw.eq(MfaCode::getOtpAscii ,mfaCode.getOtpAscii());
        }
        if (StringUtils.isNotBlank(mfaCode.getOtpHex())){
            lqw.eq(MfaCode::getOtpHex ,mfaCode.getOtpHex());
        }
        if (StringUtils.isNotBlank(mfaCode.getOtpBase32())){
            lqw.eq(MfaCode::getOtpBase32 ,mfaCode.getOtpBase32());
        }
        if (StringUtils.isNotBlank(mfaCode.getOtpAuthUrl())){
            lqw.eq(MfaCode::getOtpAuthUrl ,mfaCode.getOtpAuthUrl());
        }
        if (mfaCode.getExpireAt() != null){
            lqw.eq(MfaCode::getExpireAt ,mfaCode.getExpireAt());
        }
        if (mfaCode.getCreatedAt() != null){
            lqw.eq(MfaCode::getCreatedAt ,mfaCode.getCreatedAt());
        }
        if (mfaCode.getCreatedBy() != null){
            lqw.eq(MfaCode::getCreatedBy ,mfaCode.getCreatedBy());
        }
        if (mfaCode.getDeletedAt() != null){
            lqw.eq(MfaCode::getDeletedAt ,mfaCode.getDeletedAt());
        }
        if (mfaCode.getDeletedBy() != null){
            lqw.eq(MfaCode::getDeletedBy ,mfaCode.getDeletedBy());
        }
        if (mfaCode.getDeleted() != null){
            lqw.eq(MfaCode::getDeleted ,mfaCode.getDeleted());
        }
        return this.list(lqw);
    }




}
