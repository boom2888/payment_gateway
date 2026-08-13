package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.PayoutInfoMapper;
import com.games.payment.domain.PayoutInfo;
import com.games.payment.service.IPayoutInfoService;

import java.util.List;
import java.util.Map;

/**
 * 支付信息Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class PayoutInfoServiceImpl extends ServiceImpl<PayoutInfoMapper, PayoutInfo> implements IPayoutInfoService {

    /**
     * 查询支付信息列表
     *
     * @param payoutInfo 支付信息
     * @return 支付信息
     */
    @Override
    public List<PayoutInfo> selectAllList(PayoutInfo payoutInfo)
    {
        return getBaseMapper().selectAllList(payoutInfo);
    }


    @Override
    public List<PayoutInfo> queryList(PayoutInfo payoutInfo) {
        LambdaQueryWrapper<PayoutInfo> lqw = Wrappers.lambdaQuery();
        if (payoutInfo.getOrderId() != null){
            lqw.eq(PayoutInfo::getOrderId ,payoutInfo.getOrderId());
        }
        if (payoutInfo.getProcessingFee() != null){
            lqw.eq(PayoutInfo::getProcessingFee ,payoutInfo.getProcessingFee());
        }
        if (payoutInfo.getDeductPayoutAmount() != null){
            lqw.eq(PayoutInfo::getDeductPayoutAmount ,payoutInfo.getDeductPayoutAmount());
        }
        if (payoutInfo.getDeductFee() != null){
            lqw.eq(PayoutInfo::getDeductFee ,payoutInfo.getDeductFee());
        }
        if (payoutInfo.getDeductRate() != null){
            lqw.eq(PayoutInfo::getDeductRate ,payoutInfo.getDeductRate());
        }
        if (payoutInfo.getMerchantCurrencyId() != null){
            lqw.eq(PayoutInfo::getMerchantCurrencyId ,payoutInfo.getMerchantCurrencyId());
        }
        if (payoutInfo.getCreatedAt() != null){
            lqw.eq(PayoutInfo::getCreatedAt ,payoutInfo.getCreatedAt());
        }
        if (payoutInfo.getUpdatedAt() != null){
            lqw.eq(PayoutInfo::getUpdatedAt ,payoutInfo.getUpdatedAt());
        }
        return this.list(lqw);
    }




}
