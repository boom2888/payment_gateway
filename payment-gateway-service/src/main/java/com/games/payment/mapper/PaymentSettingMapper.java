package com.games.payment.mapper;

import com.games.payment.domain.PaymentSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 支付设置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface PaymentSettingMapper extends BaseMapper<PaymentSetting> {
    /**
     * 查询支付设置列表
     *
     * @param paymentSetting 支付设置
     * @return 支付设置集合
     */
    List<PaymentSetting> selectAllList(PaymentSetting paymentSetting);

}
