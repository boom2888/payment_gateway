package com.games.payment.service;

import com.games.payment.domain.PaymentsData;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 支付数据Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IPaymentsDataService extends IService<PaymentsData> {

    /**
     * 查询支付数据列表
     *
     * @param paymentsData 支付数据
     * @return 支付数据集合
     */
    List<PaymentsData> selectAllList(PaymentsData paymentsData);

    /**
     * 查询列表
     */
    List<PaymentsData> queryList(PaymentsData paymentsData);

}
