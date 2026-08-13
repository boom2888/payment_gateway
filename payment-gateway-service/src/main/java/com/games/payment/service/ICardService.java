package com.games.payment.service;

import com.games.payment.domain.Card;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 客户卡令牌Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICardService extends IService<Card> {

    Card getCardByCardNumber(String cardNumber, Long acquirerId);

    Card getCardByCardNumberAndUserID(String cardNumber, Long userId, Long acquirerId);

    Long getCustomerIdByCardNumberAndUserID(String cardNumber, Long userID, Long acquirerId, Long merchantId);

    List<Card> getCardsByCardNumber(String cardNumber, Long acquirerId);

    /**
     * 根据卡指纹查询卡列表（按创建时间倒序，最新的在前面）
     *
     * @param cardFinger 卡指纹
     * @param acquirerId 收单机构ID
     * @return 卡列表（按创建时间倒序）
     */
    List<Card> getCardsByCardFinger(String cardFinger, Long acquirerId);

    /**
     * 查询客户卡令牌列表
     *
     * @param customerCardToken 客户卡令牌
     * @return 客户卡令牌集合
     */
    List<Card> selectAllList(Card customerCardToken);

    /**
     * 查询列表
     */
    List<Card> queryList(Card customerCardToken);

}
