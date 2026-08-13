package com.games.payment.service;

import com.games.payment.domain.OffRampingCustomerWallet;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 出金客户钱包Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IOffRampingCustomerWalletService extends IService<OffRampingCustomerWallet> {

    /**
     * 查询出金客户钱包列表
     *
     * @param offRampingCustomerWallet 出金客户钱包
     * @return 出金客户钱包集合
     */
    List<OffRampingCustomerWallet> selectAllList(OffRampingCustomerWallet offRampingCustomerWallet);

    /**
     * 查询列表
     */
    List<OffRampingCustomerWallet> queryList(OffRampingCustomerWallet offRampingCustomerWallet);

}
