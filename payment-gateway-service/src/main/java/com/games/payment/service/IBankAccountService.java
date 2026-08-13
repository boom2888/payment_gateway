package com.games.payment.service;

import com.games.payment.domain.BankAccount;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 银行账户Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IBankAccountService extends IService<BankAccount> {

    /**
     * 查询银行账户列表
     *
     * @param bankAccount 银行账户
     * @return 银行账户集合
     */
    List<BankAccount> selectAllList(BankAccount bankAccount);

    /**
     * 查询列表
     */
    List<BankAccount> queryList(BankAccount bankAccount);

}
