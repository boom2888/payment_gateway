package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.common.core.domain.AjaxResult;
import com.games.payment.domain.WhiteUploadRecord;
import com.games.payment.domain.WhitelistedCreditCards;

import java.util.List;
import java.util.Set;

/**
 * 白名单信用卡Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IWhitelistedCreditCardsService extends IService<WhitelistedCreditCards> {

    /**
     * 查询白名单信用卡列表
     *
     * @param whitelistedCreditCards 白名单信用卡
     * @return 白名单信用卡集合
     */
    List<WhitelistedCreditCards> selectAllList(WhitelistedCreditCards whitelistedCreditCards);

    /**
     * 查询列表
     */
    List<WhitelistedCreditCards> queryList(WhitelistedCreditCards whitelistedCreditCards);

    void importData(List<WhitelistedCreditCards> dataList, Integer importType, WhiteUploadRecord whiteUploadRecord);

    AjaxResult updateWhiteList(WhitelistedCreditCards whitelistedCreditCards);

    /**
     * 根据卡号匹配白名单（精确匹配优先，其次 BIN+后四位）
     *
     * @param shopIds   候选商户ID集合
     * @param cardNumber 纯数字卡号
     * @return 匹配到的白名单列表
     */
    List<WhitelistedCreditCards> listMatchesByCardNumber(Set<Long> shopIds, String cardNumber);

    /**
     * 根据卡指纹匹配白名单（精确匹配）
     *
     * @param shopIds    候选商户ID集合
     * @param cardFinger 卡指纹
     * @return 匹配到的白名单列表
     */
    List<WhitelistedCreditCards> listMatchesByCardFinger(Set<Long> shopIds, String cardFinger);

    List<WhitelistedCreditCards> selectByCardFinger(String cardFinger, Long shopId);

    List<WhitelistedCreditCards> selectByFullNumber(String fullNumber, Long shopId);

    List<WhitelistedCreditCards> selectByBinNumber(String binNumber, String lastFourNumber, Long shopId);
}
