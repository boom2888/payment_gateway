package com.games.payment.mapper;

import com.games.payment.domain.PayoutInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 支付信息Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface PayoutInfoMapper extends BaseMapper<PayoutInfo> {
    /**
     * 查询支付信息列表
     *
     * @param payoutInfo 支付信息
     * @return 支付信息集合
     */
    List<PayoutInfo> selectAllList(PayoutInfo payoutInfo);

}
