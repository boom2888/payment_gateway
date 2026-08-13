package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SettlementLedgerMapper;
import com.games.payment.domain.SettlementLedger;
import com.games.payment.service.ISettlementLedgerService;

import java.util.List;
import java.util.Map;

/**
 * 结算分类账Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SettlementLedgerServiceImpl extends ServiceImpl<SettlementLedgerMapper, SettlementLedger> implements ISettlementLedgerService {

    /**
     * 查询结算分类账列表
     *
     * @param settlementLedger 结算分类账
     * @return 结算分类账
     */
    @Override
    public List<SettlementLedger> selectAllList(SettlementLedger settlementLedger)
    {
        return getBaseMapper().selectAllList(settlementLedger);
    }


    @Override
    public List<SettlementLedger> queryList(SettlementLedger settlementLedger) {
        LambdaQueryWrapper<SettlementLedger> lqw = Wrappers.lambdaQuery();
        if (settlementLedger.getReconId() != null){
            lqw.eq(SettlementLedger::getReconId ,settlementLedger.getReconId());
        }
        if (settlementLedger.getMerchantId() != null){
            lqw.eq(SettlementLedger::getMerchantId ,settlementLedger.getMerchantId());
        }
        if (settlementLedger.getSettlementAmount() != null){
            lqw.eq(SettlementLedger::getSettlementAmount ,settlementLedger.getSettlementAmount());
        }
        if (settlementLedger.getChargebacksRefundsAmount() != null){
            lqw.eq(SettlementLedger::getChargebacksRefundsAmount ,settlementLedger.getChargebacksRefundsAmount());
        }
        if (settlementLedger.getRollingReserveDeducted() != null){
            lqw.eq(SettlementLedger::getRollingReserveDeducted ,settlementLedger.getRollingReserveDeducted());
        }
        if (settlementLedger.getRollingReservePayout() != null){
            lqw.eq(SettlementLedger::getRollingReservePayout ,settlementLedger.getRollingReservePayout());
        }
        if (settlementLedger.getPayout() != null){
            lqw.eq(SettlementLedger::getPayout ,settlementLedger.getPayout());
        }
        if (StringUtils.isNotBlank(settlementLedger.getNotes())){
            lqw.eq(SettlementLedger::getNotes ,settlementLedger.getNotes());
        }
        if (settlementLedger.getCreatedAt() != null){
            lqw.eq(SettlementLedger::getCreatedAt ,settlementLedger.getCreatedAt());
        }
        if (settlementLedger.getCreatedBy() != null){
            lqw.eq(SettlementLedger::getCreatedBy ,settlementLedger.getCreatedBy());
        }
        if (settlementLedger.getDeletedAt() != null){
            lqw.eq(SettlementLedger::getDeletedAt ,settlementLedger.getDeletedAt());
        }
        if (settlementLedger.getDeletedBy() != null){
            lqw.eq(SettlementLedger::getDeletedBy ,settlementLedger.getDeletedBy());
        }
        return this.list(lqw);
    }




}
