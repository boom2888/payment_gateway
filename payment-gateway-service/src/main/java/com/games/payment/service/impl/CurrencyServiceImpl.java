package com.games.payment.service.impl;

import com.games.common.enums.YesNo;
import com.games.payment.domain.Crypto;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.CurrencyMapper;
import com.games.payment.domain.Currency;
import com.games.payment.service.ICurrencyService;

import java.util.List;
import java.util.Map;

/**
 * 货币Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements ICurrencyService {

    @Override
    public List<Currency> getUseAbleList() {
        LambdaQueryWrapper<Currency> lqw = Wrappers.lambdaQuery();
        lqw.eq(Currency::getState, YesNo.YES.getValue());
        return list(lqw);
    }

    @Override
    public Currency getByCode(String currency) {
        LambdaQueryWrapper<Currency> lqw = Wrappers.lambdaQuery();
        lqw.like(Currency::getCode, currency);
        List<Currency> cryptoList = this.list(lqw);
        if (!cryptoList.isEmpty()) {
            return cryptoList.get(0);
        }
        return null;
    }

    /**
     * 查询货币列表
     *
     * @param currency 货币
     * @return 货币
     */
    @Override
    public List<Currency> selectAllList(Currency currency)
    {
        return getBaseMapper().selectAllList(currency);
    }


    @Override
    public List<Currency> queryList(Currency currency) {
        LambdaQueryWrapper<Currency> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(currency.getCode())){
            lqw.eq(Currency::getCode ,currency.getCode());
        }
        if (StringUtils.isNotBlank(currency.getSymbol())){
            lqw.eq(Currency::getSymbol ,currency.getSymbol());
        }
        if (StringUtils.isNotBlank(currency.getName())){
            lqw.like(Currency::getName ,currency.getName());
        }
        return this.list(lqw);
    }




}
