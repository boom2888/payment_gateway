package com.games.payment.mapper;

import com.games.payment.domain.MerchantPayoutIp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 商户支付IPMapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface MerchantPayoutIpMapper extends BaseMapper<MerchantPayoutIp> {
    /**
     * 查询商户支付IP列表
     *
     * @param merchantPayoutIp 商户支付IP
     * @return 商户支付IP集合
     */
    List<MerchantPayoutIp> selectAllList(MerchantPayoutIp merchantPayoutIp);

}
