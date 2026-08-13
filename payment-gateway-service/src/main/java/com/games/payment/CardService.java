package com.games.payment;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.games.common.exception.BusinessException;
import com.games.common.utils.SignUtil;
import com.games.payment.domain.BlacklistedCreditCards;
import com.games.payment.domain.Shop;
import com.games.payment.domain.WhitelistedCreditCards;
import com.games.payment.enums.WhiteListOnType;
import com.games.payment.service.IBlacklistedCreditCardsService;
import com.games.payment.service.IWhitelistedCreditCardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CardService {
    @Autowired
    IBlacklistedCreditCardsService blackCardService;

    @Resource
    IWhitelistedCreditCardsService whiteCardService;

    public boolean isBlackCard(String cardNumber){
        String cardFinger = null;
        try {
            cardFinger = SignUtil.cardFingerprint(cardNumber);
        } catch (Exception ignored) {
        }
        return isBlackCard(cardFinger, cardNumber);
    }

    public boolean isBlackCard(String cardFinger, String cardNumber){
        if (StrUtil.isNotBlank(cardFinger)) {
            LambdaQueryWrapper<BlacklistedCreditCards> fingerWrapper = Wrappers.lambdaQuery();
            fingerWrapper.eq(BlacklistedCreditCards::getCardFinger, cardFinger);
            if (blackCardService.count(fingerWrapper) > 0) {
                return true;
            }
        }
        if (StrUtil.isBlank(cardNumber) || cardNumber.length() < 10) {
            return false;
        }
        String bin = cardNumber.substring(0, 6);
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        LambdaQueryWrapper<BlacklistedCreditCards> lqw = Wrappers.lambdaQuery();
        lqw.eq(BlacklistedCreditCards::getBinNumber, bin);
        lqw.eq(BlacklistedCreditCards::getLastFourNumber, last4);
        List<BlacklistedCreditCards> cardsList = blackCardService.list(lqw);
        return !cardsList.isEmpty();
    }

    public boolean isWhiteCard(String cardNumber, Shop shop) {
        String cardFinger = null;
        try {
            cardFinger = SignUtil.cardFingerprint(cardNumber);
        } catch (Exception ignored) {
        }
        return isWhiteCard(cardFinger, cardNumber, shop);
    }

    public boolean isWhiteCard(String cardFinger, String cardNumber, Shop shop) {
        if (shop.getIsWhitelistOn() == WhiteListOnType.OFF.getValue()) {
            return true;
        } else if (shop.getIsWhitelistOn() == WhiteListOnType.ON.getValue()) {
            if (StrUtil.isNotBlank(cardFinger)) {
                List<WhitelistedCreditCards> whitelists = whiteCardService.selectByCardFinger(cardFinger, shop.getId());
                if (!whitelists.isEmpty()) {
                    log.info("命中白名单 - 卡指纹匹配: shopId={}, cardFinger={}", shop.getId(), cardFinger);
                    return true;
                }
            }
            String fullNumber = cardNumber != null ? cardNumber.trim() : null;
            if (StrUtil.isNotBlank(fullNumber)) {
                List<WhitelistedCreditCards> whitelists = whiteCardService.selectByFullNumber(fullNumber, shop.getId());
                if (!whitelists.isEmpty()) {
                    log.info("命中白名单 - 卡号匹配: shopId={}, cardNumber={}", shop.getId(), SignUtil.maskPan(cardNumber));
                    return true;
                }
            }
            if (StrUtil.isBlank(cardNumber) || cardNumber.length() < 10) {
                return false;
            }
            String bin = cardNumber.substring(0, 6);
            String last4 = cardNumber.substring(cardNumber.length() - 4);
            List<WhitelistedCreditCards> cardsList = whiteCardService.selectByBinNumber(bin, last4, shop.getId());
            if (!cardsList.isEmpty()) {
                log.info("命中白名单 - BIN+后四位匹配: shopId={}, bin={}, last4={}", shop.getId(), bin, last4);
            }
            return !cardsList.isEmpty();
        } else {
            throw new BusinessException("Whitelist switch data error");
        }
    }

}
