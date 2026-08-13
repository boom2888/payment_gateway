package com.games.payment.mapper;

import com.games.payment.domain.CustomerWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 客户钱包Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CustomerWalletMapper extends BaseMapper<CustomerWallet> {
    /**
     * 查询客户钱包列表
     *
     * @param customerWallet 客户钱包
     * @return 客户钱包集合
     */
    List<CustomerWallet> selectAllList(CustomerWallet customerWallet);

}
