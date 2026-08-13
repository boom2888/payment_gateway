package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.KycInfoMapper;
import com.games.payment.domain.KycInfo;
import com.games.payment.service.IKycInfoService;

import java.util.List;
import java.util.Map;

/**
 * KYC信息Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class KycInfoServiceImpl extends ServiceImpl<KycInfoMapper, KycInfo> implements IKycInfoService {

    /**
     * 查询KYC信息列表
     *
     * @param kycInfo KYC信息
     * @return KYC信息
     */
    @Override
    public List<KycInfo> selectAllList(KycInfo kycInfo)
    {
        return getBaseMapper().selectAllList(kycInfo);
    }


    @Override
    public List<KycInfo> queryList(KycInfo kycInfo) {
        LambdaQueryWrapper<KycInfo> lqw = Wrappers.lambdaQuery();
        if (kycInfo.getUserId() != null){
            lqw.eq(KycInfo::getUserId ,kycInfo.getUserId());
        }
        if (StringUtils.isNotBlank(kycInfo.getFirstName())){
            lqw.like(KycInfo::getFirstName ,kycInfo.getFirstName());
        }
        if (StringUtils.isNotBlank(kycInfo.getMiddleName())){
            lqw.like(KycInfo::getMiddleName ,kycInfo.getMiddleName());
        }
        if (StringUtils.isNotBlank(kycInfo.getLastName())){
            lqw.like(KycInfo::getLastName ,kycInfo.getLastName());
        }
        if (kycInfo.getBirthday() != null){
            lqw.eq(KycInfo::getBirthday ,kycInfo.getBirthday());
        }
        if (StringUtils.isNotBlank(kycInfo.getNationality())){
            lqw.eq(KycInfo::getNationality ,kycInfo.getNationality());
        }
        if (kycInfo.getDocumentType() != null){
            lqw.eq(KycInfo::getDocumentType ,kycInfo.getDocumentType());
        }
        if (StringUtils.isNotBlank(kycInfo.getDocumentNo())){
            lqw.eq(KycInfo::getDocumentNo ,kycInfo.getDocumentNo());
        }
        if (StringUtils.isNotBlank(kycInfo.getDocumentFrontUrl())){
            lqw.eq(KycInfo::getDocumentFrontUrl ,kycInfo.getDocumentFrontUrl());
        }
        if (StringUtils.isNotBlank(kycInfo.getDocumentBackUrl())){
            lqw.eq(KycInfo::getDocumentBackUrl ,kycInfo.getDocumentBackUrl());
        }
        if (StringUtils.isNotBlank(kycInfo.getDocumentHandyUrl())){
            lqw.eq(KycInfo::getDocumentHandyUrl ,kycInfo.getDocumentHandyUrl());
        }
        if (StringUtils.isNotBlank(kycInfo.getKycPayload())){
            lqw.eq(KycInfo::getKycPayload ,kycInfo.getKycPayload());
        }
        if (StringUtils.isNotBlank(kycInfo.getKycAmlPayload())){
            lqw.eq(KycInfo::getKycAmlPayload ,kycInfo.getKycAmlPayload());
        }
        if (kycInfo.getCreatedAt() != null){
            lqw.eq(KycInfo::getCreatedAt ,kycInfo.getCreatedAt());
        }
        if (kycInfo.getCreatedBy() != null){
            lqw.eq(KycInfo::getCreatedBy ,kycInfo.getCreatedBy());
        }
        if (kycInfo.getDeletedAt() != null){
            lqw.eq(KycInfo::getDeletedAt ,kycInfo.getDeletedAt());
        }
        if (kycInfo.getDeletedBy() != null){
            lqw.eq(KycInfo::getDeletedBy ,kycInfo.getDeletedBy());
        }
        if (kycInfo.getDeleted() != null){
            lqw.eq(KycInfo::getDeleted ,kycInfo.getDeleted());
        }
        if (kycInfo.getSsoKyc() != null){
            lqw.eq(KycInfo::getSsoKyc ,kycInfo.getSsoKyc());
        }
        return this.list(lqw);
    }




}
