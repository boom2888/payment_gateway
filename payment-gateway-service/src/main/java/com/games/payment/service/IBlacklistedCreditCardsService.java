package com.games.payment.service;

import com.games.payment.domain.BlacklistedCreditCards;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 黑名单信用卡Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IBlacklistedCreditCardsService extends IService<BlacklistedCreditCards> {

    /**
     * 查询黑名单信用卡列表
     *
     * @param blacklistedCreditCards 黑名单信用卡
     * @return 黑名单信用卡集合
     */
    List<BlacklistedCreditCards> selectAllList(BlacklistedCreditCards blacklistedCreditCards);

    /**
     * 查询列表
     */
    List<BlacklistedCreditCards> queryList(BlacklistedCreditCards blacklistedCreditCards);
    
    /**
     * 逻辑删除黑名单信用卡
     *
     * @param ids 需要删除的黑名单信用卡ID
     * @return 结果
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 根据ID获取黑名单信用卡（确保返回脱敏格式）
     *
     * @param id 黑名单信用卡ID
     * @return 黑名单信用卡
     */
    BlacklistedCreditCards getById(Long id);

}