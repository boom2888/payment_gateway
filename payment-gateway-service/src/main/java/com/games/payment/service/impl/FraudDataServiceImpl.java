package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.FraudDataMapper;
import com.games.payment.domain.FraudData;
import com.games.payment.service.IFraudDataService;

import java.util.List;
import java.util.Map;

/**
 * 欺诈数据Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class FraudDataServiceImpl extends ServiceImpl<FraudDataMapper, FraudData> implements IFraudDataService {

    /**
     * 查询欺诈数据列表
     *
     * @param fraudData 欺诈数据
     * @return 欺诈数据
     */
    @Override
    public List<FraudData> selectAllList(FraudData fraudData)
    {
        return getBaseMapper().selectAllList(fraudData);
    }


    @Override
    public List<FraudData> queryList(FraudData fraudData) {
        LambdaQueryWrapper<FraudData> lqw = Wrappers.lambdaQuery();
        if (fraudData.getUploadId() != null){
            lqw.eq(FraudData::getUploadId ,fraudData.getUploadId());
        }
        if (fraudData.getReportFraudulentId() != null){
            lqw.eq(FraudData::getReportFraudulentId ,fraudData.getReportFraudulentId());
        }
        if (fraudData.getBin() != null){
            lqw.eq(FraudData::getBin ,fraudData.getBin());
        }
        if (fraudData.getIssueDate() != null){
            lqw.eq(FraudData::getIssueDate ,fraudData.getIssueDate());
        }
        if (fraudData.getUsdAmount() != null){
            lqw.eq(FraudData::getUsdAmount ,fraudData.getUsdAmount());
        }
        if (fraudData.getMethodName() != null){
            lqw.like(FraudData::getMethodName ,fraudData.getMethodName());
        }
        if (fraudData.getTrackId() != null){
            lqw.eq(FraudData::getTrackId ,fraudData.getTrackId());
        }
        if (fraudData.getTransactionDate() != null){
            lqw.eq(FraudData::getTransactionDate ,fraudData.getTransactionDate());
        }
        if (fraudData.getCreatedAt() != null){
            lqw.eq(FraudData::getCreatedAt ,fraudData.getCreatedAt());
        }
        if (fraudData.getUpdatedAt() != null){
            lqw.eq(FraudData::getUpdatedAt ,fraudData.getUpdatedAt());
        }
        if (StringUtils.isNotBlank(fraudData.getSsoOrderId())){
            lqw.eq(FraudData::getSsoOrderId ,fraudData.getSsoOrderId());
        }
        if (fraudData.getMerchantId() != null){
            lqw.eq(FraudData::getMerchantId ,fraudData.getMerchantId());
        }
        if (fraudData.getCurrencyId() != null){
            lqw.eq(FraudData::getCurrencyId ,fraudData.getCurrencyId());
        }
        if (fraudData.getFiatAmount() != null){
            lqw.eq(FraudData::getFiatAmount ,fraudData.getFiatAmount());
        }
        if (fraudData.getStatus() != null){
            lqw.eq(FraudData::getStatus ,fraudData.getStatus());
        }
        if (fraudData.getOrderCreated() != null){
            lqw.eq(FraudData::getOrderCreated ,fraudData.getOrderCreated());
        }
        if (fraudData.getCryptoAmount() != null){
            lqw.eq(FraudData::getCryptoAmount ,fraudData.getCryptoAmount());
        }
        if (StringUtils.isNotBlank(fraudData.getIssuerCountry())){
            lqw.eq(FraudData::getIssuerCountry ,fraudData.getIssuerCountry());
        }
        return this.list(lqw);
    }




}
