package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.common.enums.YesNo;
import com.games.payment.domain.Crypto;
import com.games.payment.mapper.CryptoCurrencyMapper;
import com.games.payment.service.ICryptoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 加密货币Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CryptoCurrencyServiceImpl extends ServiceImpl<CryptoCurrencyMapper, Crypto> implements ICryptoService {

    @Override
    public Crypto getBySymbol(String cryptoCurrency) {
        LambdaQueryWrapper<Crypto> lqw = Wrappers.lambdaQuery();
        lqw.like(Crypto::getShortName ,cryptoCurrency);
        List<Crypto> cryptoList = this.list(lqw);
        if (!cryptoList.isEmpty()) {
            return cryptoList.get(0);
        }
        return null;
    }


    @Override
    public List<Crypto> getUseAbleList() {
        LambdaQueryWrapper<Crypto> lqw = Wrappers.lambdaQuery();
        lqw.eq(Crypto::getState, YesNo.YES.getValue());
        return list(lqw);
    }

    /**
     * 查询加密货币列表
     *
     * @param cryptoCurrency 加密货币
     * @return 加密货币
     */
    @Override
    public List<Crypto> selectAllList(Crypto cryptoCurrency)
    {
        return getBaseMapper().selectAllList(cryptoCurrency);
    }


    @Override
    public List<Crypto> queryList(Crypto cryptoCurrency) {
        LambdaQueryWrapper<Crypto> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(cryptoCurrency.getShortName())){
            lqw.like(Crypto::getShortName ,cryptoCurrency.getShortName());
        }
        if (StringUtils.isNotBlank(cryptoCurrency.getName())){
            lqw.like(Crypto::getName ,cryptoCurrency.getName());
        }
        if (StringUtils.isNotBlank(cryptoCurrency.getChain())){
            lqw.eq(Crypto::getChain ,cryptoCurrency.getChain());
        }
        if (StringUtils.isNotBlank(cryptoCurrency.getB2c2Ticker())){
            lqw.eq(Crypto::getB2c2Ticker ,cryptoCurrency.getB2c2Ticker());
        }
        if (cryptoCurrency.getDecimalPrecision() != null){
            lqw.eq(Crypto::getDecimalPrecision ,cryptoCurrency.getDecimalPrecision());
        }
        if (cryptoCurrency.getIsUtxoBased() != null){
            lqw.eq(Crypto::getIsUtxoBased ,cryptoCurrency.getIsUtxoBased());
        }
        if (StringUtils.isNotBlank(cryptoCurrency.getFireblocksAssetId())){
            lqw.eq(Crypto::getFireblocksAssetId ,cryptoCurrency.getFireblocksAssetId());
        }
        return this.list(lqw);
    }



}
