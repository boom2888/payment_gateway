package com.games.payment.mapper;

import com.games.payment.domain.BlacklistedCreditCards;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 黑名单信用卡Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface BlacklistedCreditCardsMapper extends BaseMapper<BlacklistedCreditCards> {
    /**
     * 查询黑名单信用卡列表
     *
     * @param blacklistedCreditCards 黑名单信用卡
     * @return 黑名单信用卡集合
     */
    List<BlacklistedCreditCards> selectAllList(BlacklistedCreditCards blacklistedCreditCards);

}