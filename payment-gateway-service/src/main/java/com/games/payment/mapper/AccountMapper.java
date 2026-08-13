package com.games.payment.mapper;

import com.games.payment.domain.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 账户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-07
 */
public interface AccountMapper extends BaseMapper<Account> {
    /**
     * 查询账户列表
     *
     * @param account 账户
     * @return 账户集合
     */
    List<Account> selectAllList(Account account);

}
