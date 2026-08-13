package com.games.payment.service;

import com.games.payment.domain.SettlementLedger;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 结算分类账Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISettlementLedgerService extends IService<SettlementLedger> {

    /**
     * 查询结算分类账列表
     *
     * @param settlementLedger 结算分类账
     * @return 结算分类账集合
     */
    List<SettlementLedger> selectAllList(SettlementLedger settlementLedger);

    /**
     * 查询列表
     */
    List<SettlementLedger> queryList(SettlementLedger settlementLedger);

}
