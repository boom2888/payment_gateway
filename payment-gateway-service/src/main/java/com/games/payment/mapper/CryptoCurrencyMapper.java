package com.games.payment.mapper;

import com.games.payment.domain.Crypto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 加密货币Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CryptoCurrencyMapper extends BaseMapper<Crypto> {
    /**
     * 查询加密货币列表
     *
     * @param cryptoCurrency 加密货币
     * @return 加密货币集合
     */
    List<Crypto> selectAllList(Crypto cryptoCurrency);

}
