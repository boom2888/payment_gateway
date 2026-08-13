package com.games.payment.mapper;

import com.games.payment.domain.BankAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 银行账户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface BankAccountMapper extends BaseMapper<BankAccount> {
    /**
     * 查询银行账户列表
     *
     * @param bankAccount 银行账户
     * @return 银行账户集合
     */
    List<BankAccount> selectAllList(BankAccount bankAccount);

}
