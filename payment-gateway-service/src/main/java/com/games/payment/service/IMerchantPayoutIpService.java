package com.games.payment.service;

import com.games.payment.domain.MerchantPayoutIp;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 商户支付IPService接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IMerchantPayoutIpService extends IService<MerchantPayoutIp> {

    /**
     * 查询商户支付IP列表
     *
     * @param merchantPayoutIp 商户支付IP
     * @return 商户支付IP集合
     */
    List<MerchantPayoutIp> selectAllList(MerchantPayoutIp merchantPayoutIp);

    /**
     * 查询列表
     */
    List<MerchantPayoutIp> queryList(MerchantPayoutIp merchantPayoutIp);

}
