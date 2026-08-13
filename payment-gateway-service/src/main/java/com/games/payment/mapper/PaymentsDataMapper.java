package com.games.payment.mapper;

import com.games.payment.domain.PaymentsData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 支付数据Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface PaymentsDataMapper extends BaseMapper<PaymentsData> {
    /**
     * 查询支付数据列表
     *
     * @param paymentsData 支付数据
     * @return 支付数据集合
     */
    List<PaymentsData> selectAllList(PaymentsData paymentsData);

}
