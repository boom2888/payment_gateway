package com.games.payment.mapper;

import com.games.payment.domain.HouseWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 平台钱包Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface HouseWalletMapper extends BaseMapper<HouseWallet> {
    /**
     * 查询平台钱包列表
     *
     * @param houseWallet 平台钱包
     * @return 平台钱包集合
     */
    List<HouseWallet> selectAllList(HouseWallet houseWallet);

}
