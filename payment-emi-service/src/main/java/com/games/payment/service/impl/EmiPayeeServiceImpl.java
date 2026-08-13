package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.EmiPayeeMapper;
import com.games.payment.domain.EmiPayee;
import com.games.payment.service.IEmiPayeeService;

import java.util.List;
import java.util.Map;

/**
 * 收款方列Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Service
public class EmiPayeeServiceImpl extends ServiceImpl<EmiPayeeMapper, EmiPayee> implements IEmiPayeeService {

    /**
     * 查询收款方列列表
     *
     * @param emiPayee 收款方列
     * @return 收款方列
     */
    @Override
    public List<EmiPayee> selectAllList(EmiPayee emiPayee)
    {
        return getBaseMapper().selectAllList(emiPayee);
    }


    @Override
    public List<EmiPayee> queryList(EmiPayee emiPayee) {
        LambdaQueryWrapper<EmiPayee> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(emiPayee.getAccount())){
            lqw.eq(EmiPayee::getAccount ,emiPayee.getAccount());
        }
        if (StringUtils.isNotBlank(emiPayee.getBankOfDeposit())){
            lqw.eq(EmiPayee::getBankOfDeposit ,emiPayee.getBankOfDeposit());
        }
        if (StringUtils.isNotBlank(emiPayee.getAccountName())){
            lqw.like(EmiPayee::getAccountName ,emiPayee.getAccountName());
        }
        if (StringUtils.isNotBlank(emiPayee.getAccountType())){
            lqw.eq(EmiPayee::getAccountType ,emiPayee.getAccountType());
        }
        if (StringUtils.isNotBlank(emiPayee.getStatus())){
            lqw.eq(EmiPayee::getStatus ,emiPayee.getStatus());
        }
        if (StringUtils.isNotBlank(emiPayee.getUse())){
            lqw.eq(EmiPayee::getUse ,emiPayee.getUse());
        }
        return this.list(lqw);
    }




}
