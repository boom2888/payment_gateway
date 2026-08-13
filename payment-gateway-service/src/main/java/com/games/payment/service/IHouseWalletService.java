package com.games.payment.service;

import com.games.payment.domain.HouseWallet;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 平台钱包Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IHouseWalletService extends IService<HouseWallet> {

    /**
     * 查询平台钱包列表
     *
     * @param houseWallet 平台钱包
     * @return 平台钱包集合
     */
    List<HouseWallet> selectAllList(HouseWallet houseWallet);

    /**
     * 查询列表
     */
    List<HouseWallet> queryList(HouseWallet houseWallet);

}
