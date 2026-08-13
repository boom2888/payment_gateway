package com.games.payment.service;

import com.games.payment.domain.Currency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 货币Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICurrencyService extends IService<Currency> {

    List<Currency> getUseAbleList();

    Currency getByCode(String currency);

    /**
     * 查询货币列表
     *
     * @param currency 货币
     * @return 货币集合
     */
    List<Currency> selectAllList(Currency currency);

    /**
     * 查询列表
     */
    List<Currency> queryList(Currency currency);


}
