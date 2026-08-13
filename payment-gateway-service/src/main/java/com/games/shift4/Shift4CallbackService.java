package com.games.shift4;

import com.games.payment.domain.Order;

import java.util.Map;

/**
 * Shift4 回调订单解析
 */
public interface Shift4CallbackService {

    /**
     * 从 Shift4 回调参数中解析并定位对应订单。
     *
     * @param params Shift4 回调 form 参数
     * @return 订单，不存在时返回 null
     */
    Order resolveCallbackOrder(Map<String, String> params);
}

