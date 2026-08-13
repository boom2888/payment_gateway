package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.EmiPaymentHistoryMapper;
import com.games.payment.domain.EmiPaymentHistory;
import com.games.payment.service.IEmiPaymentHistoryService;

import java.util.List;
// removed unused import

/**
 * 付款记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Service
public class EmiPaymentHistoryServiceImpl extends ServiceImpl<EmiPaymentHistoryMapper, EmiPaymentHistory> implements IEmiPaymentHistoryService {

    /**
     * 查询付款记录列表
     *
     * @param emiPaymentHistory 付款记录
     * @return 付款记录
     */
    @Override
    public List<EmiPaymentHistory> selectAllList(EmiPaymentHistory emiPaymentHistory)
    {
        return getBaseMapper().selectAllList(emiPaymentHistory);
    }


    @Override
    public List<EmiPaymentHistory> queryList(EmiPaymentHistory emiPaymentHistory) {
        LambdaQueryWrapper<EmiPaymentHistory> lqw = Wrappers.lambdaQuery();
        if (emiPaymentHistory.getBeginTime() != null && emiPaymentHistory.getEndTime() != null) {
            lqw.between(EmiPaymentHistory::getPayTime, emiPaymentHistory.getBeginTime(), emiPaymentHistory.getEndTime());
        } else if (emiPaymentHistory.getBeginTime() != null) {
            lqw.ge(EmiPaymentHistory::getPayTime, emiPaymentHistory.getBeginTime());
        } else if (emiPaymentHistory.getEndTime() != null) {
            lqw.le(EmiPaymentHistory::getPayTime, emiPaymentHistory.getEndTime());
        } else if (emiPaymentHistory.getPayTime() != null) {
            lqw.eq(EmiPaymentHistory::getPayTime ,emiPaymentHistory.getPayTime());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getWithdrawalNumber())){
            lqw.eq(EmiPaymentHistory::getWithdrawalNumber ,emiPaymentHistory.getWithdrawalNumber());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getBeneficiaryBank())){
            lqw.eq(EmiPaymentHistory::getBeneficiaryBank ,emiPaymentHistory.getBeneficiaryBank());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getPayee())){
            lqw.eq(EmiPaymentHistory::getPayee ,emiPaymentHistory.getPayee());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getPaymentAccountNumber())){
            lqw.eq(EmiPaymentHistory::getPaymentAccountNumber ,emiPaymentHistory.getPaymentAccountNumber());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getPaymentAmount())){
            lqw.eq(EmiPaymentHistory::getPaymentAmount ,emiPaymentHistory.getPaymentAmount());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getAmountReceived())){
            lqw.eq(EmiPaymentHistory::getAmountReceived ,emiPaymentHistory.getAmountReceived());
        }
        if (StringUtils.isNotBlank(emiPaymentHistory.getStatus())){
            lqw.eq(EmiPaymentHistory::getStatus ,emiPaymentHistory.getStatus());
        }
        return this.list(lqw);
    }




}
