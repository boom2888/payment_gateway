package com.games.payment.service;

import com.games.payment.domain.CustomerWallet;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 客户钱包Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICustomerWalletService extends IService<CustomerWallet> {
    CustomerWallet getCustomerWalletByCrypto(Long customerId, Long cryptoId);

    /**
     * 查询客户钱包列表
     *
     * @param customerWallet 客户钱包
     * @return 客户钱包集合
     */
    List<CustomerWallet> selectAllList(CustomerWallet customerWallet);

    /**
     * 查询列表
     */
    List<CustomerWallet> queryList(CustomerWallet customerWallet);


}
