package com.games.payment.service;

import com.games.payment.domain.ShopLimit;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 商户限额Service接口
 *
 * @author Ticker
 * @date 2025-09-24
 */
public interface IShopLimitService extends IService<ShopLimit> {


    ShopLimit getByShopId(Long shopId);

    /** 是否存在指定商户的限额记录 */
    boolean existsByShopId(Long shopId);

    /** 指定商户限额是否存在（排除某条记录） */
    boolean existsByShopIdExceptId(Long shopId, Long excludeId);
    /**
     * 查询商户限额列表
     *
     * @param shopLimit 商户限额
     * @return 商户限额集合
     */
    List<ShopLimit> selectAllList(ShopLimit shopLimit);

    /**
     * 查询列表
     */
    List<ShopLimit> queryList(ShopLimit shopLimit);

}
