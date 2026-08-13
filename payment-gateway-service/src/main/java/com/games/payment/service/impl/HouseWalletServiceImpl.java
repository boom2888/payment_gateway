package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.HouseWalletMapper;
import com.games.payment.domain.HouseWallet;
import com.games.payment.service.IHouseWalletService;

import java.util.List;
import java.util.Map;

/**
 * 平台钱包Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class HouseWalletServiceImpl extends ServiceImpl<HouseWalletMapper, HouseWallet> implements IHouseWalletService {

    /**
     * 查询平台钱包列表
     *
     * @param houseWallet 平台钱包
     * @return 平台钱包
     */
    @Override
    public List<HouseWallet> selectAllList(HouseWallet houseWallet)
    {
        return getBaseMapper().selectAllList(houseWallet);
    }


    @Override
    public List<HouseWallet> queryList(HouseWallet houseWallet) {
        LambdaQueryWrapper<HouseWallet> lqw = Wrappers.lambdaQuery();
        if (houseWallet.getCryptoId() != null){
            lqw.eq(HouseWallet::getCryptoId ,houseWallet.getCryptoId());
        }
        if (StringUtils.isNotBlank(houseWallet.getWalletAddress())){
            lqw.eq(HouseWallet::getWalletAddress ,houseWallet.getWalletAddress());
        }
        if (houseWallet.getDeleted() != null){
            lqw.eq(HouseWallet::getDeleted ,houseWallet.getDeleted());
        }
        if (StringUtils.isNotBlank(houseWallet.getWalletName())){
            lqw.like(HouseWallet::getWalletName ,houseWallet.getWalletName());
        }
        if (houseWallet.getSaasUserCorporationId() != null){
            lqw.eq(HouseWallet::getSaasUserCorporationId ,houseWallet.getSaasUserCorporationId());
        }
        if (houseWallet.getIsDefault() != null){
            lqw.eq(HouseWallet::getIsDefault ,houseWallet.getIsDefault());
        }
        return this.list(lqw);
    }




}
