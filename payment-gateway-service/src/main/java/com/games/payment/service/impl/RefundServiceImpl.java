package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.api.enums.RefundStatus;
import com.games.payment.domain.RefundOrder;
import com.games.payment.mapper.RefundMapper;
import com.games.payment.service.IRefundOrderService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, RefundOrder> implements IRefundOrderService {


    @Override
    public RefundOrder selectByOrderId(Long orderId) {
        LambdaQueryWrapper<RefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(RefundOrder::getOrderId, orderId);
        List<RefundOrder> orderList = list(lqw);
        if (!orderList.isEmpty()) return orderList.get(0);
        return null;
    }

    @Override
    public RefundOrder selectSuccessOrderId(Long orderId) {
        LambdaQueryWrapper<RefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(RefundOrder::getOrderId, orderId);
        lqw.eq(RefundOrder::getStatus, RefundStatus.SUCCESS.name());
        lqw.orderByDesc(RefundOrder::getId);
        List<RefundOrder> orderList = list(lqw);
        if (!orderList.isEmpty()) return orderList.get(0);
        return null;
    }

    @Override
    public RefundOrder selectByRefundSsoId(String ssoOrderId) {
        LambdaQueryWrapper<RefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(RefundOrder::getRefundSsoId, ssoOrderId);
        List<RefundOrder> orderList = list(lqw);
        if (!orderList.isEmpty()) return orderList.get(0);
        return null;
    }

    @Override
    public RefundOrder selectByRefundSsoIdAndOrderId(String refundSsoId, Long orderId) {
        LambdaQueryWrapper<RefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(RefundOrder::getRefundSsoId, refundSsoId);
        lqw.eq(RefundOrder::getOrderId, orderId);
        List<RefundOrder> orderList = list(lqw);
        if (!orderList.isEmpty()) return orderList.get(0);
        return null;
    }


    @Override
    public List<RefundOrder> selectAllList(RefundOrder refundOrder) {
        return  getBaseMapper().selectAllList(refundOrder);
    }

    @Override
    public List<RefundOrder> queryList(RefundOrder refundOrder) {
        LambdaQueryWrapper<RefundOrder> lqw = Wrappers.lambdaQuery();
        if (refundOrder.getOrderId() != null) {
            lqw.eq(RefundOrder::getId, refundOrder.getId());
        }
        if (refundOrder.getOrderId() != null) {
            lqw.eq(RefundOrder::getOrderId, refundOrder.getOrderId());
        }
        if (refundOrder.getRefundAmount() != null) {
            lqw.eq(RefundOrder::getRefundAmount, refundOrder.getRefundAmount());
        }
        if (refundOrder.getRefundCurrencyId() != null) {
            lqw.eq(RefundOrder::getRefundCurrencyId, refundOrder.getRefundCurrencyId());
        }
        if (refundOrder.getStatus() != null) {
            lqw.eq(RefundOrder::getStatus, refundOrder.getStatus());
        }
        if (refundOrder.getRemainingRefundableAmount() != null) {
            lqw.eq(RefundOrder::getRemainingRefundableAmount, refundOrder.getRemainingRefundableAmount());
        }
        if (refundOrder.getFailReason() != null) {
            lqw.eq(RefundOrder::getFailReason, refundOrder.getFailReason());
        }
        if (refundOrder.getCreateAt() != null) {
            lqw.eq(RefundOrder::getCreateAt, refundOrder.getCreateAt());
        }
        return this.list(lqw);
    }

    @Override
    public Map<String, Integer> countRefundOrdersByDate(Long merchantId, String startDate, String endDate) {
        // 使用SQL查询，避免内存问题
        List<Map<String, Object>> resultList = getBaseMapper().countRefundOrdersByDate(
                merchantId,
                startDate + " 00:00:00",
                endDate + " 23:59:59"
        );

        // 转换为Map<String, Integer>格式
        Map<String, Integer> result = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            Object dateValue = row.get("date");
            LocalDate date;
            if (dateValue instanceof Date) {
                date = ((Date) dateValue).toLocalDate();
            } else if (dateValue instanceof LocalDate) {
                date = (LocalDate) dateValue;
            } else if (dateValue != null) {
                date = LocalDate.parse(dateValue.toString());
            } else {
                continue;
            }

            Number countNumber = (Number) row.get("cnt");
            result.put(date.toString(), countNumber.intValue());
        }

        return result;
    }

    @Override
    public Map<String, java.math.BigDecimal> sumRefundAmountByDate(Long merchantId, String startDate, String endDate) {
        List<Map<String, Object>> resultList = getBaseMapper().sumRefundAmountByDate(
                merchantId,
                startDate + " 00:00:00",
                endDate + " 23:59:59"
        );
        Map<String, java.math.BigDecimal> result = new HashMap<>();
        for (Map<String, Object> row : resultList) {
            Object dateValue = row.get("date");
            LocalDate date;
            if (dateValue instanceof Date) {
                date = ((Date) dateValue).toLocalDate();
            } else if (dateValue instanceof LocalDate) {
                date = (LocalDate) dateValue;
            } else if (dateValue != null) {
                date = LocalDate.parse(dateValue.toString());
            } else {
                continue;
            }
            java.math.BigDecimal amount = row.get("amount") == null ? java.math.BigDecimal.ZERO : new java.math.BigDecimal(row.get("amount").toString());
            result.put(date.toString(), amount);
        }
        return result;
    }
}
