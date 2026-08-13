package com.games.payment.mapper;

import com.games.payment.domain.Vip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * VIPMapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface VipMapper extends BaseMapper<Vip> {
    /**
     * 查询VIP列表
     *
     * @param vip VIP
     * @return VIP集合
     */
    List<Vip> selectAllList(Vip vip);

}
