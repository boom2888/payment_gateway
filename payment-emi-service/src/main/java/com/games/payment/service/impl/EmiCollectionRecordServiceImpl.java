package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.EmiCollectionRecordMapper;
import com.games.payment.domain.EmiCollectionRecord;
import com.games.payment.service.IEmiCollectionRecordService;

import java.util.List;
import java.util.Map;

/**
 * 收款记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Service
public class EmiCollectionRecordServiceImpl extends ServiceImpl<EmiCollectionRecordMapper, EmiCollectionRecord> implements IEmiCollectionRecordService {

    /**
     * 查询收款记录列表
     *
     * @param emiCollectionRecord 收款记录
     * @return 收款记录
     */
    @Override
    public List<EmiCollectionRecord> selectAllList(EmiCollectionRecord emiCollectionRecord)
    {
        return getBaseMapper().selectAllList(emiCollectionRecord);
    }


    @Override
    public List<EmiCollectionRecord> queryList(EmiCollectionRecord emiCollectionRecord) {
        LambdaQueryWrapper<EmiCollectionRecord> lqw = Wrappers.lambdaQuery();
        if (emiCollectionRecord.getCollectionTime() != null){
            lqw.eq(EmiCollectionRecord::getCollectionTime ,emiCollectionRecord.getCollectionTime());
        }
        if (StringUtils.isNotBlank(emiCollectionRecord.getReceiptNumber())){
            lqw.eq(EmiCollectionRecord::getReceiptNumber ,emiCollectionRecord.getReceiptNumber());
        }
        if (StringUtils.isNotBlank(emiCollectionRecord.getApplicationAmount())){
            lqw.eq(EmiCollectionRecord::getApplicationAmount ,emiCollectionRecord.getApplicationAmount());
        }
        if (StringUtils.isNotBlank(emiCollectionRecord.getPaymentAmount())){
            lqw.eq(EmiCollectionRecord::getPaymentAmount ,emiCollectionRecord.getPaymentAmount());
        }
        if (StringUtils.isNotBlank(emiCollectionRecord.getAmountEntered())){
            lqw.eq(EmiCollectionRecord::getAmountEntered ,emiCollectionRecord.getAmountEntered());
        }
        if (StringUtils.isNotBlank(emiCollectionRecord.getPaymentStatus())){
            lqw.eq(EmiCollectionRecord::getPaymentStatus ,emiCollectionRecord.getPaymentStatus());
        }
        return this.list(lqw);
    }




}
