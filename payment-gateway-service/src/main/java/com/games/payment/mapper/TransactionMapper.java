package com.games.payment.mapper;

import com.games.payment.domain.Transaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 交易Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface TransactionMapper extends BaseMapper<Transaction> {
    /**
     * 查询交易列表
     *
     * @param transaction 交易
     * @return 交易集合
     */
    List<Transaction> selectAllList(Transaction transaction);

}
