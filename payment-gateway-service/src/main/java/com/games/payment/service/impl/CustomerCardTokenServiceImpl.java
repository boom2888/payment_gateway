package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.common.utils.SignUtil;
import com.games.payment.domain.Card;
import com.games.payment.mapper.CustomerCardTokenMapper;
import com.games.payment.service.ICardService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 客户卡令牌Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CustomerCardTokenServiceImpl extends ServiceImpl<CustomerCardTokenMapper, Card> implements ICardService {

    @Override
    public Card getCardByCardNumber(String cardNumber, Long acquirerId) {
        if (StringUtils.isBlank(cardNumber) || acquirerId == null) {
            return null;
        }
        String cardFinger = SignUtil.cardFingerprint(cardNumber);
        LambdaQueryWrapper<Card> lqw = Wrappers.lambdaQuery();
        lqw.eq(Card::getAcquirerId, acquirerId);
        lqw.eq(Card::getCardFinger, cardFinger);
        List<Card> cryptoList = this.list(lqw);
        if (!cryptoList.isEmpty()) {
            return cryptoList.get(0);
        }
        return null;
    }

    @Override
    public Card getCardByCardNumberAndUserID(String cardNumber, Long userId, Long acquirerId) {
        if (StringUtils.isBlank(cardNumber) || userId == null || acquirerId == null) {
            return null;
        }
        String cardFinger = SignUtil.cardFingerprint(cardNumber);
        return baseMapper.selectByCardFingerAndUserID(cardFinger, userId, acquirerId);
    }

    @Override
    public Long getCustomerIdByCardNumberAndUserID(String cardNumber, Long userID, Long acquirerId, Long merchantId) {
        if (StringUtils.isBlank(cardNumber) || userID == null || acquirerId == null || merchantId == null) {
            return null;
        }
        String cardFinger = SignUtil.cardFingerprint(cardNumber);
        return baseMapper.selectCustomerId(cardFinger, userID, acquirerId, merchantId);
    }

    @Override
    public List<Card> getCardsByCardNumber(String cardNumber, Long acquirerId) {
        if (StringUtils.isBlank(cardNumber) || acquirerId == null) {
            return Collections.emptyList();
        }
        String cardFinger = SignUtil.cardFingerprint(cardNumber);
        LambdaQueryWrapper<Card> lqw = Wrappers.lambdaQuery();
        lqw.eq(Card::getCardFinger, cardFinger);
        lqw.eq(Card::getAcquirerId, acquirerId);
        lqw.orderByAsc(Card::getCardFinger);
        return this.list(lqw);
    }

    @Override
    public List<Card> getCardsByCardFinger(String cardFinger, Long acquirerId) {
        if (StringUtils.isBlank(cardFinger) || acquirerId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Card> lqw = Wrappers.lambdaQuery();
        lqw.eq(Card::getCardFinger, cardFinger);
        lqw.eq(Card::getAcquirerId, acquirerId);
        lqw.orderByDesc(Card::getCreatedAt); // 按创建时间倒序，最新的在前面
        return this.list(lqw);
    }

    @Override
    public boolean save(Card entity) {
        fillCardFingerIfPossible(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(Card entity) {
        fillCardFingerIfPossible(entity);
        return super.updateById(entity);
    }

    private void fillCardFingerIfPossible(Card card) {
        if (card == null) {
            return;
        }
        String rawCardNumber = StringUtils.trimToEmpty(card.getCardNumber());
        if (StringUtils.isBlank(rawCardNumber)) {
            return;
        }
        String normalized = rawCardNumber.replaceAll("[\\s-]", "");
        if (!normalized.matches("\\d{16,19}")) {
            return;
        }
        String bin = normalized.substring(0, 6);
        String last4 = normalized.substring(normalized.length() - 4);
        if (StringUtils.isBlank(card.getCardFinger())) {
            card.setCardFinger(SignUtil.cardFingerprint(normalized));
        }
        if (StringUtils.isBlank(card.getBin())) {
            card.setBin(bin);
        }
        int maskLength = Math.max(0, normalized.length() - 10);
        String maskedNumber = bin + StringUtils.repeat("*", maskLength) + last4;
        card.setCardNumber(maskedNumber);
    }

    /**
     * 查询客户卡令牌列表
     *
     * @param customerCardToken 客户卡令牌
     * @return 客户卡令牌
     */
    @Override
    public List<Card> selectAllList(Card customerCardToken) {
        return getBaseMapper().selectAllList(customerCardToken);
    }

    @Override
    public List<Card> queryList(Card customerCardToken) {
        LambdaQueryWrapper<Card> lqw = Wrappers.lambdaQuery();
        if (customerCardToken.getUserId() != null) {
            lqw.eq(Card::getUserId, customerCardToken.getUserId());
        }
        if (customerCardToken.getAcquirerId() != null) {
            lqw.eq(Card::getAcquirerId, customerCardToken.getAcquirerId());
        }
        if (StringUtils.isNotBlank(customerCardToken.getToken())) {
            lqw.eq(Card::getToken, customerCardToken.getToken());
        }
        if (StringUtils.isNotBlank(customerCardToken.getCardNumber())) {
            lqw.eq(Card::getCardNumber, customerCardToken.getCardNumber());
        }
        if (customerCardToken.getCreatedAt() != null) {
            lqw.eq(Card::getCreatedAt, customerCardToken.getCreatedAt());
        }
        if (customerCardToken.getCreatedBy() != null) {
            lqw.eq(Card::getCreatedBy, customerCardToken.getCreatedBy());
        }
        if (customerCardToken.getDeletedAt() != null) {
            lqw.eq(Card::getDeletedAt, customerCardToken.getDeletedAt());
        }
        if (customerCardToken.getDeletedBy() != null) {
            lqw.eq(Card::getDeletedBy, customerCardToken.getDeletedBy());
        }
        if (customerCardToken.getDeleted() != null) {
            lqw.eq(Card::getDeleted, customerCardToken.getDeleted());
        }
        if (StringUtils.isNotBlank(customerCardToken.getCardType())) {
            lqw.eq(Card::getCardType, customerCardToken.getCardType());
        }
        if (StringUtils.isNotBlank(customerCardToken.getCardBrand())) {
            lqw.eq(Card::getCardBrand, customerCardToken.getCardBrand());
        }
        if (customerCardToken.getExpiryMonth() != null) {
            lqw.eq(Card::getExpiryMonth, customerCardToken.getExpiryMonth());
        }
        if (customerCardToken.getExpiryYear() != null) {
            lqw.eq(Card::getExpiryYear, customerCardToken.getExpiryYear());
        }
        if (StringUtils.isNotBlank(customerCardToken.getBin())) {
            lqw.eq(Card::getBin, customerCardToken.getBin());
        }
        if (StringUtils.isNotBlank(customerCardToken.getScheme())) {
            lqw.eq(Card::getScheme, customerCardToken.getScheme());
        }
        if (StringUtils.isNotBlank(customerCardToken.getCurrency())) {
            lqw.eq(Card::getCurrency, customerCardToken.getCurrency());
        }
        if (StringUtils.isNotBlank(customerCardToken.getIssuer())) {
            lqw.eq(Card::getIssuer, customerCardToken.getIssuer());
        }
        if (StringUtils.isNotBlank(customerCardToken.getIssuerCountry())) {
            lqw.eq(Card::getIssuerCountry, customerCardToken.getIssuerCountry());
        }
        if (StringUtils.isNotBlank(customerCardToken.getCardholderName())) {
            lqw.like(Card::getCardholderName, customerCardToken.getCardholderName());
        }
        if (StringUtils.isNotBlank(customerCardToken.getCardholderEmailAddress())) {
            lqw.eq(Card::getCardholderEmailAddress, customerCardToken.getCardholderEmailAddress());
        }
        return this.list(lqw);
    }

}
