package com.games.payment.mapper;

import com.games.payment.domain.PayToCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 支付到卡Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface PayToCardMapper extends BaseMapper<PayToCard> {
    /**
     * 查询支付到卡列表
     *
     * @param payToCard 支付到卡
     * @return 支付到卡集合
     */
    List<PayToCard> selectAllList(PayToCard payToCard);

}
