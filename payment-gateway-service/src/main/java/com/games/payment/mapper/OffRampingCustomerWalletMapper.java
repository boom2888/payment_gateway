package com.games.payment.mapper;

import com.games.payment.domain.OffRampingCustomerWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 出金客户钱包Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface OffRampingCustomerWalletMapper extends BaseMapper<OffRampingCustomerWallet> {
    /**
     * 查询出金客户钱包列表
     *
     * @param offRampingCustomerWallet 出金客户钱包
     * @return 出金客户钱包集合
     */
    List<OffRampingCustomerWallet> selectAllList(OffRampingCustomerWallet offRampingCustomerWallet);

}
