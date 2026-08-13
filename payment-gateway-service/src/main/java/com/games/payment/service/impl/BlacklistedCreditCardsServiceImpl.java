package com.games.payment.service.impl;

import com.games.common.exception.BusinessException;
import com.games.common.utils.CardNumberUtils;
import com.games.common.utils.SignUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.BlacklistedCreditCardsMapper;
import com.games.payment.domain.BlacklistedCreditCards;
import com.games.payment.service.IBlacklistedCreditCardsService;

import java.util.List;

/**
 * 黑名单信用卡Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class BlacklistedCreditCardsServiceImpl extends ServiceImpl<BlacklistedCreditCardsMapper, BlacklistedCreditCards> implements IBlacklistedCreditCardsService {

    /**
     * 查询黑名单信用卡列表
     *
     * @param blacklistedCreditCards 黑名单信用卡
     * @return 黑名单信用卡
     */
    @Override
    public List<BlacklistedCreditCards> selectAllList(BlacklistedCreditCards blacklistedCreditCards)
    {
        List<BlacklistedCreditCards> list = getBaseMapper().selectAllList(blacklistedCreditCards);
        // 确保返回的卡号是脱敏格式
        if (list != null) {
            for (BlacklistedCreditCards card : list) {
                if (card != null && StringUtils.isNotBlank(card.getFullNumber())) {
                    card.setFullNumber(CardNumberUtils.ensureMasked(card.getFullNumber()));
                }
            }
        }
        return list;
    }


    @Override
    public List<BlacklistedCreditCards> queryList(BlacklistedCreditCards blacklistedCreditCards) {
        LambdaQueryWrapper<BlacklistedCreditCards> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(blacklistedCreditCards.getFullNumber())){
            lqw.eq(BlacklistedCreditCards::getFullNumber ,blacklistedCreditCards.getFullNumber());
        }
        if (StringUtils.isNotBlank(blacklistedCreditCards.getBinNumber())){
            lqw.eq(BlacklistedCreditCards::getBinNumber ,blacklistedCreditCards.getBinNumber());
        }
        if (StringUtils.isNotBlank(blacklistedCreditCards.getLastFourNumber())){
            lqw.eq(BlacklistedCreditCards::getLastFourNumber ,blacklistedCreditCards.getLastFourNumber());
        }
        if (blacklistedCreditCards.getCreatedAt() != null){
            lqw.eq(BlacklistedCreditCards::getCreatedAt ,blacklistedCreditCards.getCreatedAt());
        }
        if (blacklistedCreditCards.getCreatedBy() != null){
            lqw.eq(BlacklistedCreditCards::getCreatedBy ,blacklistedCreditCards.getCreatedBy());
        }
        if (blacklistedCreditCards.getDeletedAt() != null){
            lqw.eq(BlacklistedCreditCards::getDeletedAt ,blacklistedCreditCards.getDeletedAt());
        }
        if (blacklistedCreditCards.getDeletedBy() != null){
            lqw.eq(BlacklistedCreditCards::getDeletedBy ,blacklistedCreditCards.getDeletedBy());
        }
        if (blacklistedCreditCards.getDeleted() != null){
            lqw.eq(BlacklistedCreditCards::getDeleted ,blacklistedCreditCards.getDeleted());
        }
        List<BlacklistedCreditCards> list = this.list(lqw);
        // 确保返回的卡号是脱敏格式
        if (list != null) {
            for (BlacklistedCreditCards card : list) {
                if (card != null && StringUtils.isNotBlank(card.getFullNumber())) {
                    card.setFullNumber(CardNumberUtils.ensureMasked(card.getFullNumber()));
                }
            }
        }
        return list;
    }

    /**
     * 根据ID获取黑名单信用卡（确保返回脱敏格式）
     */
    @Override
    public BlacklistedCreditCards getById(Long id) {
        BlacklistedCreditCards card = super.getById(id);
        if (card != null && StringUtils.isNotBlank(card.getFullNumber())) {
            card.setFullNumber(CardNumberUtils.ensureMasked(card.getFullNumber()));
        }
        return card;
    }

    /**
     * 逻辑删除黑名单信用卡
     *
     * @param ids 需要删除的黑名单信用卡ID数组
     * @return 结果
     */
    @Override
    public boolean deleteByIds(List<Long> ids) {
        // 使用 MyBatis Plus 提供的逻辑删除功能
        return this.removeByIds(ids);
    }
    
    /**
     * 保存黑名单信用卡
     *
     * @param blacklistedCreditCards 黑名单信用卡
     * @return 结果
     */
    @Override
	    public boolean save(BlacklistedCreditCards blacklistedCreditCards) {
	        fillCardFieldsForCompliance(blacklistedCreditCards);
	        // 检查是否已存在相同的bin_number和lastFourNumber组合且未删除
	        if (StringUtils.isNotBlank(blacklistedCreditCards.getBinNumber()) && 
	            StringUtils.isNotBlank(blacklistedCreditCards.getLastFourNumber())) {
	            LambdaQueryWrapper<BlacklistedCreditCards> lqw = Wrappers.lambdaQuery();
	            lqw.eq(BlacklistedCreditCards::getBinNumber, blacklistedCreditCards.getBinNumber())
               .eq(BlacklistedCreditCards::getLastFourNumber, blacklistedCreditCards.getLastFourNumber())
               .eq(BlacklistedCreditCards::getDeleted, 0);
            
            if (this.count(lqw) > 0) {
                throw BusinessException.api("blacklist.duplicate.bin.last.four");
            }
        }
        return super.save(blacklistedCreditCards);
    }
    
    /**
     * 更新黑名单信用卡
     *
     * @param blacklistedCreditCards 黑名单信用卡
     * @return 结果
     */
	    @Override
	    public boolean updateById(BlacklistedCreditCards blacklistedCreditCards) {
	        fillCardFieldsForCompliance(blacklistedCreditCards);
	        // 检查是否已存在相同的bin_number和lastFourNumber组合且未删除（排除自己）
	        if (StringUtils.isNotBlank(blacklistedCreditCards.getBinNumber()) && 
	            StringUtils.isNotBlank(blacklistedCreditCards.getLastFourNumber())) {
	            LambdaQueryWrapper<BlacklistedCreditCards> lqw = Wrappers.lambdaQuery();
	            lqw.eq(BlacklistedCreditCards::getBinNumber, blacklistedCreditCards.getBinNumber())
               .eq(BlacklistedCreditCards::getLastFourNumber, blacklistedCreditCards.getLastFourNumber())
               .eq(BlacklistedCreditCards::getDeleted, 0)
               .ne(BlacklistedCreditCards::getId, blacklistedCreditCards.getId());
            
            if (this.count(lqw) > 0) {
                throw BusinessException.api("blacklist.duplicate.bin.last.four");
            }
	        }
	        return super.updateById(blacklistedCreditCards);
	    }

    /**
     * 合规：禁止落库完整 PAN，仅允许保存"前六****后四"。
     * 允许输入完整 PAN，用于计算 card_finger 与补齐 bin/last4。
     * 只有输入了完整卡号才需要计算 card_finger 和存储脱敏格式。
     */
    private void fillCardFieldsForCompliance(BlacklistedCreditCards blacklistedCreditCards) {
        if (blacklistedCreditCards == null) {
            return;
        }
        String inputFullNumber = blacklistedCreditCards.getFullNumber();
        if (StringUtils.isBlank(inputFullNumber)) {
            // 如果没有输入完整卡号，不设置 fullNumber，也不计算 card_finger
            return;
        }

        String normalizedDigits = CardNumberUtils.normalizeDigits(inputFullNumber);
        if (normalizedDigits.matches("\\d{16,19}")) {
            // 只有输入了完整卡号才计算 card_finger
            if (StringUtils.isBlank(blacklistedCreditCards.getCardFinger())) {
                try {
                    String cardFinger = SignUtil.cardFingerprint(normalizedDigits);
                    if (StringUtils.isNotBlank(cardFinger)) {
                        blacklistedCreditCards.setCardFinger(cardFinger);
                    }
                } catch (Exception ignored) {
                    // 忽略计算指纹失败的情况
                }
            }
            // 自动填充 bin/last4（如果未提供）
            if (StringUtils.isBlank(blacklistedCreditCards.getBinNumber())) {
                blacklistedCreditCards.setBinNumber(normalizedDigits.substring(0, 6));
            }
            if (StringUtils.isBlank(blacklistedCreditCards.getLastFourNumber())) {
                blacklistedCreditCards.setLastFourNumber(normalizedDigits.substring(normalizedDigits.length() - 4));
            }
            // 将完整卡号转换为脱敏格式存储
            blacklistedCreditCards.setFullNumber(CardNumberUtils.normalizeMaskedPanForStorage(inputFullNumber));
        } else {
            // 如果不是完整卡号，尝试规范化（可能是已脱敏的格式）
            String masked = CardNumberUtils.normalizeMaskedPanForStorage(inputFullNumber);
            if (StringUtils.isNotBlank(masked)) {
                blacklistedCreditCards.setFullNumber(masked);
            }
        }
    }
	}
