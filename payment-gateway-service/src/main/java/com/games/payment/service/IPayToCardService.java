package com.games.payment.service;

import com.games.payment.domain.PayToCard;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 支付到卡Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IPayToCardService extends IService<PayToCard> {

    /**
     * 查询支付到卡列表
     *
     * @param payToCard 支付到卡
     * @return 支付到卡集合
     */
    List<PayToCard> selectAllList(PayToCard payToCard);

    /**
     * 查询列表
     */
    List<PayToCard> queryList(PayToCard payToCard);

}
