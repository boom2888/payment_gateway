package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.RollingReserveLedgerMapper;
import com.games.payment.domain.RollingReserveLedger;
import com.games.payment.service.IRollingReserveLedgerService;

import java.util.List;
import java.util.Map;

/**
 * 滚动准备金分类账Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class RollingReserveLedgerServiceImpl extends ServiceImpl<RollingReserveLedgerMapper, RollingReserveLedger> implements IRollingReserveLedgerService {

    /**
     * 查询滚动准备金分类账列表
     *
     * @param rollingReserveLedger 滚动准备金分类账
     * @return 滚动准备金分类账
     */
    @Override
    public List<RollingReserveLedger> selectAllList(RollingReserveLedger rollingReserveLedger)
    {
        return getBaseMapper().selectAllList(rollingReserveLedger);
    }


    @Override
    public List<RollingReserveLedger> queryList(RollingReserveLedger rollingReserveLedger) {
        LambdaQueryWrapper<RollingReserveLedger> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(rollingReserveLedger.getSettlementId())){
            lqw.eq(RollingReserveLedger::getSettlementId ,rollingReserveLedger.getSettlementId());
        }
        if (rollingReserveLedger.getMerchantId() != null){
            lqw.eq(RollingReserveLedger::getMerchantId ,rollingReserveLedger.getMerchantId());
        }
        if (rollingReserveLedger.getTransactionDate() != null){
            lqw.eq(RollingReserveLedger::getTransactionDate ,rollingReserveLedger.getTransactionDate());
        }
        if (rollingReserveLedger.getTransactionType() != null){
            lqw.eq(RollingReserveLedger::getTransactionType ,rollingReserveLedger.getTransactionType());
        }
        if (rollingReserveLedger.getReference() != null){
            lqw.eq(RollingReserveLedger::getReference ,rollingReserveLedger.getReference());
        }
        if (rollingReserveLedger.getRollingBalance() != null){
            lqw.eq(RollingReserveLedger::getRollingBalance ,rollingReserveLedger.getRollingBalance());
        }
        if (rollingReserveLedger.getPayoutDate() != null){
            lqw.eq(RollingReserveLedger::getPayoutDate ,rollingReserveLedger.getPayoutDate());
        }
        return this.list(lqw);
    }




}
