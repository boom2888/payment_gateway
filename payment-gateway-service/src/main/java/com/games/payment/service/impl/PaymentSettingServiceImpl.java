package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.PaymentSettingMapper;
import com.games.payment.domain.PaymentSetting;
import com.games.payment.service.IPaymentSettingService;

import java.util.List;
import java.util.Map;

/**
 * 支付设置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class PaymentSettingServiceImpl extends ServiceImpl<PaymentSettingMapper, PaymentSetting> implements IPaymentSettingService {

    /**
     * 查询支付设置列表
     *
     * @param paymentSetting 支付设置
     * @return 支付设置
     */
    @Override
    public List<PaymentSetting> selectAllList(PaymentSetting paymentSetting)
    {
        return getBaseMapper().selectAllList(paymentSetting);
    }


    @Override
    public List<PaymentSetting> queryList(PaymentSetting paymentSetting) {
        LambdaQueryWrapper<PaymentSetting> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(paymentSetting.getKey())){
            lqw.eq(PaymentSetting::getKey ,paymentSetting.getKey());
        }
        if (StringUtils.isNotBlank(paymentSetting.getValue())){
            lqw.eq(PaymentSetting::getValue ,paymentSetting.getValue());
        }
        if (paymentSetting.getCreatedAt() != null){
            lqw.eq(PaymentSetting::getCreatedAt ,paymentSetting.getCreatedAt());
        }
        if (paymentSetting.getUpdatedAt() != null){
            lqw.eq(PaymentSetting::getUpdatedAt ,paymentSetting.getUpdatedAt());
        }
        return this.list(lqw);
    }




}
