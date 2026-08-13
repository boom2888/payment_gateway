package com.games.payment;

import cn.hutool.core.util.StrUtil;
import com.games.api.enums.OrderStatus;
import com.games.common.utils.BigUtils;
import com.games.common.utils.DateUtils;
import com.games.common.utils.TimezoneUtils;
import com.games.payment.domain.Currency;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.mapper.OrderMapper;
import com.games.payment.service.ICurrencyService;
import com.games.payment.service.IOrderService;
import com.games.payment.service.IShopService;
import com.games.tg.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PushService {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ICurrencyService currencyService;
    /** 健康检查TG群组ID */
    @Value("${tg.safeGroupId: -4856351298}")
    private String safeGroupId;
    @Resource
    private OrderMapper orderMapper;

    /**
     * 推送健康和预警消息
     * @param mesage
     */
    public void pushSafeMsg(String mesage) {
        try {
            CompletableFuture.runAsync(()-> {
                telegramService.pushMessage(safeGroupId, mesage);
            });
        }catch (Exception e) {
            log.error("推送健康预警消息失败", e);
        }
    }

    /**
     * 推送订单消息
     * @param orderId
     */
    public void pushShopOrder(Long orderId) {
        try {
            pushOneShopOrder(orderId);
        }catch (Exception e) {
            log.error("推送订单消息失败: {}", orderId, e);
        }
    }

    public void pushOneShopOrder(Long orderId) {
        CompletableFuture.runAsync(()-> {
            Order order = orderService.getById(orderId);
            if(order == null)return;
            Shop shop = shopService.getById(order.getMerchantId());
            Currency currency = currencyService.getById(order.getCurrencyId());
            String shopOrderTgGroupId = shop.getNotifyOrderStatusEndpoint3();
            if(StrUtil.isBlank(shopOrderTgGroupId))return;
            boolean isSkip = StrUtil.equals(order.getStatus(), OrderStatus.WAITING_FOR_DEPOSIT.getCode());
            if(isSkip)return;

            StringBuilder sb = new StringBuilder();
            String status = StrUtil.equals(order.getStatus(), OrderStatus.DEPOSIT_DECLINED.getCode()) ? "❌" : (StrUtil.equals(order.getStatus(), OrderStatus.COMPLETED.getCode()) ? "✅" : OrderStatus.fromCode(order.getStatus()).getDescription());
            String amount = order.getFiatAmount().setScale(2, RoundingMode.HALF_DOWN) + currency.getCode();

            sb.append(status + amount).append("\r\n");
            String error = order.getFailReasons();
            if(StrUtil.isNotBlank(error)) {
                sb.append("错误：").append(error).append("\r\n");
            }
            sb.append("单号：").append(order.getId()).append("\r\n");
            
            // 使用默认时区显示时间（TODO: 后续可以为Shop表添加timezone字段）
            String timezone = "Asia/Shanghai"; // 默认时区，后续可以根据shop.getCountry()推断时区
            String formattedTime = TimezoneUtils.formatDateInTimezone(order.getPayTime(), timezone, DateUtils.DD_HH_MM_SS);
            sb.append("时间：").append(formattedTime).append("\r\n");
            sb.append("流水：").append(order.getSsoOrderId()).append("\r\n");

            // 统计所有完成订单金额
            if (orderId % 10 == 0) {
                Map<Long, String> currencyMap = currencyService.list().stream().collect(Collectors.toMap(Currency::getId, Currency::getCode));
                // 获取今日日期（使用指定时区或默认北京时间）
                String startDatetime = DateUtils.getDate() + " 00:00:00";
                String endDatetime = DateUtils.getDate() + " 23:59:59";
                Map<Long, BigDecimal> currencyIdTotalAmountMap = orderService.getOrderTotalAmount(shop.getId(), startDatetime, endDatetime);
                if(!currencyIdTotalAmountMap.isEmpty()){

                    Long shopId = shop.getId();
//                    log.info("今日统计查询: startDatetime={}, endDatetime={}, shopId={}, timezone={}", startDatetime, endDatetime, shopId, timezone);

                    // 成功数量
                    int succOrderTotal = orderMapper.getOrderNumber(startDatetime, endDatetime, shopId, OrderStatus.getSuccessStatus());
                    int failOrderTotal = orderMapper.getOrderNumber(startDatetime, endDatetime, shopId, OrderStatus.getFailStatus());
                    //成交总额
//                    BigDecimal transferAmount = orderMapper.getOrderAmount(startDatetime, endDatetime, shopId);


//                    int succOrderTotal = orderService.getOrderTotalCount(shop.getId(), DateUtils.getTodayDate(true), DateUtils.getTodayDate(false), 1);
//                    int failOrderTotal = orderService.getOrderTotalCount(shop.getId(), DateUtils.getTodayDate(true), DateUtils.getTodayDate(false), 0);

                    StringJoiner joiner = new StringJoiner(",");
                    currencyIdTotalAmountMap.forEach((currencyId, totalAmount) -> {
                        String currencyName = currencyMap.get(currencyId);
                        joiner.add(totalAmount.setScale(2, RoundingMode.UP) + currencyName);
                    });
                    String amountBuilder = joiner.toString();

                    String succPasent = BigUtils.calcSuccessRate(succOrderTotal, failOrderTotal);
                    String info = StrUtil.format("总额:{},成功数:{},失败数:{},成功率:{}\r\n", amountBuilder, succOrderTotal, failOrderTotal, succPasent);
                    sb.append(info);
                }
            }
            telegramService.pushMessage(shopOrderTgGroupId, sb.toString());
        });

    }

}
