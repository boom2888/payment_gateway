package com.games.shift4;

import cn.hutool.core.util.StrUtil;
import com.games.payment.domain.Order;
import com.games.payment.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class Shift4CallbackServiceImpl implements Shift4CallbackService {

    private final IOrderService orderService;

    @Override
    public Order resolveCallbackOrder(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        // a1: Shift4 requestId。常见场景：
        // - 支付/3DS completion：a1 = {orderId}
        // - DFP completion：a1 = 3ds{orderId}
        String requestId = params.get("a1");
        Long orderId = parseOrderIdFromRequestId(requestId);
        if (orderId != null) {
            Order order = orderService.getById(orderId);
            if (order != null) {
                return order;
            }
        }

        // 兜底：部分流程可通过 3ds_trxid（保存到 relatedTransactionId）定位订单
        String tdsTrxId = params.get("3ds_trxid");
        if (StrUtil.isNotBlank(tdsTrxId)) {
            return orderService.getOrderByRelatedTransactionId(tdsTrxId);
        }

        return null;
    }

    private Long parseOrderIdFromRequestId(String requestId) {
        if (StrUtil.isBlank(requestId)) {
            return null;
        }

        String normalized = requestId.trim();
        if (StrUtil.startWithIgnoreCase(normalized, "3ds")) {
            normalized = normalized.substring(3);
        }

        if (!normalized.matches("\\d+")) {
            return null;
        }

        try {
            return Long.parseLong(normalized);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

