package com.games.payment.mapper;

import com.games.payment.domain.MerchantAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.MovementData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 商户账户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface MerchantAccountMapper extends BaseMapper<MerchantAccount> {
    /**
     * 查询商户账户列表
     *
     * @param merchantAccount 商户账户
     * @return 商户账户集合
     */
    List<MerchantAccount> selectAllList(MerchantAccount merchantAccount);

    void updateBalance(@Param("amountMap") Map<String, BigDecimal> amountMap);

    void rollbackBalance(@Param("merchantId") String merchantId, @Param("amount") BigDecimal amount);
}
