package com.games.payment.mapper;

import com.games.payment.domain.SettlementLedger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 结算分类账Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SettlementLedgerMapper extends BaseMapper<SettlementLedger> {
    /**
     * 查询结算分类账列表
     *
     * @param settlementLedger 结算分类账
     * @return 结算分类账集合
     */
    List<SettlementLedger> selectAllList(SettlementLedger settlementLedger);

}
