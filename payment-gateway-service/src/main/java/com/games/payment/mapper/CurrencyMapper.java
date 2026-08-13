package com.games.payment.mapper;

import com.games.payment.domain.Currency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 货币Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CurrencyMapper extends BaseMapper<Currency> {
    /**
     * 查询货币列表
     *
     * @param currency 货币
     * @return 货币集合
     */
    List<Currency> selectAllList(Currency currency);

}
