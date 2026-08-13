package com.games.payment.service;

import com.games.payment.domain.Account;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 账户Service接口
 *
 * @author Ticker
 * @date 2025-07-07
 */
public interface IAccountService extends IService<Account> {

    /**
     * 查询账户列表
     *
     * @param account 账户
     * @return 账户集合
     */
    List<Account> selectAllList(Account account);

    /**
     * 查询列表
     */
    List<Account> queryList(Account account);

}
