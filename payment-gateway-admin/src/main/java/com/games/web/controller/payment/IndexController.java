package com.games.web.controller.payment;

import com.games.common.core.domain.AjaxResult;
import com.games.pay.vo.IndexShopRankStatisticsRequest;
import com.games.payment.service.IOrderService;
import com.games.payment.TotalService;
import com.games.common.utils.TimezoneUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * 首页Controller
 * 
 * @author Ticker
 * @date 2025-08-20
 */
@RestController
@RequestMapping("/payment/index" )
public class IndexController extends BasePayController {
    @Resource
    private IOrderService orderService;
    
    @Resource
    private TotalService totalService;

    /**
     * 首页头部统计
     */
    @GetMapping("/orderStatistics")
    public AjaxResult orderStatistics() {
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(orderService.orderStatistics(isShopRole() ? getShopId() : null, timezone));
    }

    /**
     * 首页商户排名统计
     */
    @GetMapping("/shopRankStatistics")
    public AjaxResult shopRankStatistics(IndexShopRankStatisticsRequest request) {
        if (isShopRole()){
            request.setShopId(getShopId());
        }
        return AjaxResult.successData(orderService.shopRankStatistics(request));
    }

    /**
     * 首页订单数量曲线
     */
    @GetMapping("/orderNumCurve")
    public AjaxResult orderNumCurve(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(orderService.orderNumCurve(startDate, endDate, shoppId, timezone));
    }

    /**
     * 首页订单金额曲线
     */
    @GetMapping("/orderAmountCurve")
    public AjaxResult orderAmountCurve(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(orderService.orderAmountCurve(startDate, endDate, shoppId, timezone));
    }

    /**
     * 首页订单均价曲线
     */
    @GetMapping("/orderAvgAmountCurve")
    public AjaxResult orderAvgAmountCurve(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(orderService.orderAvgAmountCurve(startDate, endDate, shoppId, timezone));
    }

    /**
     * 首页订单 退款率、拒付率、取消率 曲线
     */
    @GetMapping("/orderRateCurve")
    public AjaxResult orderRateCurve(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(orderService.orderRateCurve(startDate, endDate, shoppId, timezone));
    }

    /**
     * 首页订单 支付方式
     */
    @GetMapping("/paymentMethodStats")
    public AjaxResult paymentMethodStats(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(orderService.paymentMethodStats(startDate, endDate, shoppId, timezone));
    }

    /**
     * 每小时订单成功率统计
     */
    @GetMapping("/hourlySuccessRate")
    public AjaxResult hourlySuccessRate(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        return AjaxResult.successData(totalService.getHourlyOrderSuccessRate(startDate, endDate, shoppId, timezone));
    }

    /**
     * 失败原因统计
     */
    @GetMapping("/failReasonStats")
    public AjaxResult failReasonStats(String startDate, String endDate, Long merchantId) {
        Long shoppId;
        if (isShopRole()){
            shoppId = getShopId();
        }else {
            shoppId = merchantId;
        }
        return AjaxResult.successData(orderService.getFailReasonStats(startDate, endDate, shoppId));
    }
}
