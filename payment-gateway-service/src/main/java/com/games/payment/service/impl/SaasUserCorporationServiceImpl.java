package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasUserCorporationMapper;
import com.games.payment.domain.SaasUserCorporation;
import com.games.payment.service.ISaasUserCorporationService;

import java.util.List;
import java.util.Map;

/**
 * SaaS用户公司Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SaasUserCorporationServiceImpl extends ServiceImpl<SaasUserCorporationMapper, SaasUserCorporation> implements ISaasUserCorporationService {

    /**
     * 查询SaaS用户公司列表
     *
     * @param saasUserCorporation SaaS用户公司
     * @return SaaS用户公司
     */
    @Override
    public List<SaasUserCorporation> selectAllList(SaasUserCorporation saasUserCorporation)
    {
        return getBaseMapper().selectAllList(saasUserCorporation);
    }


    @Override
    public List<SaasUserCorporation> queryList(SaasUserCorporation saasUserCorporation) {
        LambdaQueryWrapper<SaasUserCorporation> lqw = Wrappers.lambdaQuery();
        if (saasUserCorporation.getAcquirerId() != null){
            lqw.eq(SaasUserCorporation::getAcquirerId ,saasUserCorporation.getAcquirerId());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getCode())){
            lqw.eq(SaasUserCorporation::getCode ,saasUserCorporation.getCode());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getName())){
            lqw.like(SaasUserCorporation::getName ,saasUserCorporation.getName());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getDomain())){
            lqw.eq(SaasUserCorporation::getDomain ,saasUserCorporation.getDomain());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getThemeUrl())){
            lqw.eq(SaasUserCorporation::getThemeUrl ,saasUserCorporation.getThemeUrl());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getPartnerBank())){
            lqw.eq(SaasUserCorporation::getPartnerBank ,saasUserCorporation.getPartnerBank());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getPartnerBankSortCode())){
            lqw.eq(SaasUserCorporation::getPartnerBankSortCode ,saasUserCorporation.getPartnerBankSortCode());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdVerifyCode())){
            lqw.eq(SaasUserCorporation::getEmailIdVerifyCode ,saasUserCorporation.getEmailIdVerifyCode());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdVeriff())){
            lqw.eq(SaasUserCorporation::getEmailIdVeriff ,saasUserCorporation.getEmailIdVeriff());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdVeriffAddress())){
            lqw.eq(SaasUserCorporation::getEmailIdVeriffAddress ,saasUserCorporation.getEmailIdVeriffAddress());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdBank())){
            lqw.eq(SaasUserCorporation::getEmailIdBank ,saasUserCorporation.getEmailIdBank());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdForgotPwd())){
            lqw.eq(SaasUserCorporation::getEmailIdForgotPwd ,saasUserCorporation.getEmailIdForgotPwd());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdVeriffKyc())){
            lqw.eq(SaasUserCorporation::getEmailIdVeriffKyc ,saasUserCorporation.getEmailIdVeriffKyc());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getRevenueShare())){
            lqw.eq(SaasUserCorporation::getRevenueShare ,saasUserCorporation.getRevenueShare());
        }
        if (saasUserCorporation.getCreatedAt() != null){
            lqw.eq(SaasUserCorporation::getCreatedAt ,saasUserCorporation.getCreatedAt());
        }
        if (saasUserCorporation.getCreatedBy() != null){
            lqw.eq(SaasUserCorporation::getCreatedBy ,saasUserCorporation.getCreatedBy());
        }
        if (saasUserCorporation.getDeletedAt() != null){
            lqw.eq(SaasUserCorporation::getDeletedAt ,saasUserCorporation.getDeletedAt());
        }
        if (saasUserCorporation.getDeletedBy() != null){
            lqw.eq(SaasUserCorporation::getDeletedBy ,saasUserCorporation.getDeletedBy());
        }
        if (saasUserCorporation.getDeleted() != null){
            lqw.eq(SaasUserCorporation::getDeleted ,saasUserCorporation.getDeleted());
        }
        if (saasUserCorporation.getAddressId() != null){
            lqw.eq(SaasUserCorporation::getAddressId ,saasUserCorporation.getAddressId());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getNotabeneVaspDid())){
            lqw.eq(SaasUserCorporation::getNotabeneVaspDid ,saasUserCorporation.getNotabeneVaspDid());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getNotabeneClientId())){
            lqw.eq(SaasUserCorporation::getNotabeneClientId ,saasUserCorporation.getNotabeneClientId());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getNotabeneClientSecret())){
            lqw.eq(SaasUserCorporation::getNotabeneClientSecret ,saasUserCorporation.getNotabeneClientSecret());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getNotabeneClientCredentials())){
            lqw.eq(SaasUserCorporation::getNotabeneClientCredentials ,saasUserCorporation.getNotabeneClientCredentials());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailTemplateSendfrom())){
            lqw.eq(SaasUserCorporation::getEmailTemplateSendfrom ,saasUserCorporation.getEmailTemplateSendfrom());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdEmailAddressVerification())){
            lqw.eq(SaasUserCorporation::getEmailIdEmailAddressVerification ,saasUserCorporation.getEmailIdEmailAddressVerification());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getLegalPersonName())){
            lqw.like(SaasUserCorporation::getLegalPersonName ,saasUserCorporation.getLegalPersonName());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdOrderCompletionNotification())){
            lqw.eq(SaasUserCorporation::getEmailIdOrderCompletionNotification ,saasUserCorporation.getEmailIdOrderCompletionNotification());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getHouseWalletProvider())){
            lqw.eq(SaasUserCorporation::getHouseWalletProvider ,saasUserCorporation.getHouseWalletProvider());
        }
        if (saasUserCorporation.getTravelRuleCheckLimit() != null){
            lqw.eq(SaasUserCorporation::getTravelRuleCheckLimit ,saasUserCorporation.getTravelRuleCheckLimit());
        }
        if (saasUserCorporation.getTravelRuleLimitCurrencyId() != null){
            lqw.eq(SaasUserCorporation::getTravelRuleLimitCurrencyId ,saasUserCorporation.getTravelRuleLimitCurrencyId());
        }
        if (saasUserCorporation.getFireblocksWithdrawalAccountId() != null){
            lqw.eq(SaasUserCorporation::getFireblocksWithdrawalAccountId ,saasUserCorporation.getFireblocksWithdrawalAccountId());
        }
        if (saasUserCorporation.getFireblocksOperationalAccountId() != null){
            lqw.eq(SaasUserCorporation::getFireblocksOperationalAccountId ,saasUserCorporation.getFireblocksOperationalAccountId());
        }
        if (saasUserCorporation.getFireblocksHiddenOnUi() != null){
            lqw.eq(SaasUserCorporation::getFireblocksHiddenOnUi ,saasUserCorporation.getFireblocksHiddenOnUi());
        }
        if (saasUserCorporation.getFireblocksAutoFuel() != null){
            lqw.eq(SaasUserCorporation::getFireblocksAutoFuel ,saasUserCorporation.getFireblocksAutoFuel());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdOffOrderCompletionNotification())){
            lqw.eq(SaasUserCorporation::getEmailIdOffOrderCompletionNotification ,saasUserCorporation.getEmailIdOffOrderCompletionNotification());
        }
        if (StringUtils.isNotBlank(saasUserCorporation.getEmailIdOffOrderFailedNotification())){
            lqw.eq(SaasUserCorporation::getEmailIdOffOrderFailedNotification ,saasUserCorporation.getEmailIdOffOrderFailedNotification());
        }
        return this.list(lqw);
    }




}
