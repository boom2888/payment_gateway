package com.games.payment.mapper;

import com.games.payment.domain.CryptoTransaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 加密货币交易Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CryptoTransactionMapper extends BaseMapper<CryptoTransaction> {
    /**
     * 查询加密货币交易列表
     *
     * @param cryptoTransaction 加密货币交易
     * @return 加密货币交易集合
     */
    List<CryptoTransaction> selectAllList(CryptoTransaction cryptoTransaction);

}
