package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.EmiPayerTableMapper;
import com.games.payment.domain.EmiPayerTable;
import com.games.payment.service.IEmiPayerTableService;

import java.util.List;

/**
 * 付款方列Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Service
public class EmiPayerTableServiceImpl extends ServiceImpl<EmiPayerTableMapper, EmiPayerTable> implements IEmiPayerTableService {

    /**
     * 查询付款方列列表
     *
     * @param emiPayerTable 付款方列
     * @return 付款方列
     */
    @Override
    public List<EmiPayerTable> selectAllList(EmiPayerTable emiPayerTable)
    {
        return getBaseMapper().selectAllList(emiPayerTable);
    }


    @Override
    public List<EmiPayerTable> queryList(EmiPayerTable emiPayerTable) {
        LambdaQueryWrapper<EmiPayerTable> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(emiPayerTable.getPayerName())){
            lqw.like(EmiPayerTable::getPayerName ,emiPayerTable.getPayerName());
        }
        if (StringUtils.isNotBlank(emiPayerTable.getPaymentAccountName())){
            lqw.like(EmiPayerTable::getPaymentAccountName ,emiPayerTable.getPaymentAccountName());
        }
        if (StringUtils.isNotBlank(emiPayerTable.getRemitArea())){
            lqw.eq(EmiPayerTable::getRemitArea ,emiPayerTable.getRemitArea());
        }
        if (StringUtils.isNotBlank(emiPayerTable.getPaymentAccount())){
            lqw.eq(EmiPayerTable::getPaymentAccount ,emiPayerTable.getPaymentAccount());
        }
        if (StringUtils.isNotBlank(emiPayerTable.getPaymentBank())){
            lqw.eq(EmiPayerTable::getPaymentBank ,emiPayerTable.getPaymentBank());
        }
        if (StringUtils.isNotBlank(emiPayerTable.getUse())){
            lqw.eq(EmiPayerTable::getUse ,emiPayerTable.getUse());
        }
        if (StringUtils.isNotBlank(emiPayerTable.getType())){
            lqw.eq(EmiPayerTable::getType ,emiPayerTable.getType());
        }
        return this.list(lqw);
    }




}
