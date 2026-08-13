package com.games.payment;

import cn.hutool.core.util.StrUtil;
import com.games.api.enums.OrderStatus;
import com.games.common.utils.DateUtils;
import com.games.pay.vo.ShopEntity;
import com.games.pay.vo.TotalFiatAmountVo;
import com.games.payment.domain.Shop;
import com.games.payment.mapper.OrderMapper;
import com.games.payment.service.ICurrencyService;
import com.games.payment.service.IOrderService;
import com.games.payment.service.IShopService;
import com.games.tg.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.games.payment.domain.Currency;
import java.util.StringJoiner;
import java.util.Calendar;
import java.util.Date;
import com.games.pay.vo.HourlySuccessRateVo;

import jakarta.annotation.Resource;

import static com.games.common.constant.Constants.LINE;

@Slf4j
@Service
public class TotalService {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ICurrencyService currencyService;
    @Resource
    private OrderMapper orderMapper;

    public void pushShopTotalAmount(){
        List<Shop> runingShops = shopService.getRuningShops();

        StringBuilder sb = new StringBuilder();
        sb.append("今日（" + DateUtils.getDate()  + "）统计:" + LINE);
        Map<Long, String> currencyMap = currencyService.list().stream()
                .collect(Collectors.toMap(Currency::getId, Currency::getCode));
        for (Shop runingShop : runingShops) {
            // 统计所有完成订单金额
            String startDatetime = DateUtils.getDate() + " 00:00:00";
            String endDatetime = DateUtils.getDate() + " 23:59:59";
            Map<Long, BigDecimal> currencyIdTotalAmountMap = orderService.getOrderTotalAmount(runingShop.getId(), startDatetime, endDatetime);

//            Map<Long, BigDecimal> currencyIdTotalAmountMap = orderService.getOrderTotalAmount(runingShop.getId(), DateUtils.getTodayDate(true), DateUtils.getTodayDate(false));
            if(currencyIdTotalAmountMap.isEmpty())continue;
            // 成功数量
            int succOrderTotal = orderMapper.getOrderNumber(startDatetime, endDatetime, runingShop.getId(), OrderStatus.getSuccessStatus());
            int failOrderTotal = orderMapper.getOrderNumber(startDatetime, endDatetime, runingShop.getId(), OrderStatus.getFailStatus());

//            int succOrderTotal = orderService.getOrderTotalCount(runingShop.getId(), DateUtils.getTodayDate(true), DateUtils.getTodayDate(false), 1);
//            int failOrderTotal = orderService.getOrderTotalCount(runingShop.getId(), DateUtils.getTodayDate(true), DateUtils.getTodayDate(false), 0);

            StringJoiner joiner = new StringJoiner(",");
            currencyIdTotalAmountMap.forEach((currencyId, totalAmount) -> {
                String currency = currencyMap.get(currencyId);
                joiner.add(totalAmount.setScale(2, RoundingMode.UP) + currency);
            });
            String amountBuilder = joiner.toString();

            String info = StrUtil.format("商户:{}\r\n收款总额:{}\r\n成功单数:{}\r\n失败单数:{}\r\n", runingShop.getBusinessName(), amountBuilder, succOrderTotal, failOrderTotal);
            sb.append(info+"\r\n");
        }
        telegramService.pushTotalMessage(sb.toString());
    }

    /**
     * 获取每小时订单成功率统计
     * 
     * @param startDate 开始日期（yyyy-MM-dd）
     * @param endDate 结束日期（yyyy-MM-dd）
     * @param shopId 商户ID，null表示查询所有商户
     * @param timezone 客户端时区，如"Asia/Shanghai"，null则使用默认时区
     * @return 每小时成功率统计数据
     */
    public HourlySuccessRateVo getHourlyOrderSuccessRate(String startDate, String endDate, Long shopId, String timezone) {
        // 如果没有指定时间范围，默认查询最近一个月
        if (StrUtil.isBlank(startDate) || StrUtil.isBlank(endDate)) {
            Date now = new Date();
            endDate = DateUtils.parseDateToStr("yyyy-MM-dd", now);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, -30);  // 默认最近30天
            startDate = DateUtils.parseDateToStr("yyyy-MM-dd", cal.getTime());
        }
        
        // 构建时间字符串（北京时间）
        String startDatetime = startDate + " 00:00:00";
        
        // 如果结束日期是今天，则只查询到当前小时
        String endDatetime;
        String todayStr = DateUtils.parseDateToStr("yyyy-MM-dd", new Date());
        if (endDate.equals(todayStr)) {
            // 获取当前小时，设置为当前小时的59分59秒
            Calendar nowCal = Calendar.getInstance();
            int currentHour = nowCal.get(Calendar.HOUR_OF_DAY);
            endDatetime = endDate + " " + String.format("%02d:59:59", currentHour);
            log.info("今天查询，当前小时: {}, 结束时间: {}", currentHour, endDatetime);
        } else {
            // 如果不是今天，则查询到当天的23:59:59
            endDatetime = endDate + " 23:59:59";
            log.info("非今天查询，结束时间: {}", endDatetime);
        }
        
        // 添加调试日志
        log.info("每小时成功率查询参数: startDatetime={}, endDatetime={}, shopId={}", startDatetime, endDatetime, shopId);
        
        // 调用OrderService获取每小时成功率统计
        return orderService.getHourlySuccessRate(startDatetime, endDatetime, shopId, timezone);
    }

}
