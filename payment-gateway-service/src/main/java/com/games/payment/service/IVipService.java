package com.games.payment.service;

import com.games.payment.domain.Vip;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * VIPService接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IVipService extends IService<Vip> {

    /**
     * 查询VIP列表
     *
     * @param vip VIP
     * @return VIP集合
     */
    List<Vip> selectAllList(Vip vip);

    /**
     * 查询列表
     */
    List<Vip> queryList(Vip vip);

}
