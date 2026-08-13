package com.games.payment.mapper;

import com.games.payment.domain.ShopLimit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 商户限额Mapper接口
 *
 * @author Ticker
 * @date 2025-09-24
 */
public interface ShopLimitMapper extends BaseMapper<ShopLimit> {
    /**
     * 查询商户限额列表
     *
     * @param shopLimit 商户限额
     * @return 商户限额集合
     */
    List<ShopLimit> selectAllList(ShopLimit shopLimit);

}
