package com.games.payment.service;

import com.games.payment.domain.PayoutInfo;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 支付信息Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IPayoutInfoService extends IService<PayoutInfo> {

    /**
     * 查询支付信息列表
     *
     * @param payoutInfo 支付信息
     * @return 支付信息集合
     */
    List<PayoutInfo> selectAllList(PayoutInfo payoutInfo);

    /**
     * 查询列表
     */
    List<PayoutInfo> queryList(PayoutInfo payoutInfo);

}
