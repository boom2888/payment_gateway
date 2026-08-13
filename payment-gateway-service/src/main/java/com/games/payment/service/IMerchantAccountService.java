package com.games.payment.service;

import com.games.payment.domain.MerchantAccount;
import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.MovementData;

import java.util.List;

/**
 * 商户账户Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IMerchantAccountService extends IService<MerchantAccount> {

    /**
     * 查询商户账户列表
     *
     * @param merchantAccount 商户账户
     * @return 商户账户集合
     */
    List<MerchantAccount> selectAllList(MerchantAccount merchantAccount);

    /**
     * 查询列表
     */
    List<MerchantAccount> queryList(MerchantAccount merchantAccount);

    void updateBalance(List<MovementData> merchantSettlementList);
    void rollbackBalance(List<MovementData> merchantSettlementList);
}
