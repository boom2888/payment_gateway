package com.games.nuvei;

import cn.hutool.core.util.StrUtil;
import com.games.api.vo.PayOrderVo;
import com.safecharge.model.BrowserDetails;
import com.safecharge.model.Card;
import com.safecharge.model.UrlDetails;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NuveiCons {
    public static final String BASE_CURRENCY = "BASE_CURRENCY";
    //3DS回掉
    public static final String TDS_BACK_URL = "/api/callBack/3ds";
    //订单状态回掉
    public static final String NOTIFICATION_URL = "/api/callBack/order";

    public static final String SUCCESS_URL = "/success";
    public static final String FAILURE_URL = "/error";
    public static final String MERCHANT_URL = "http://www.The-Merchant-Website-Fully-Quallified-URL.com";

    public static final BigDecimal QUANTITY = BigDecimal.valueOf(0.01D);

    public static final BigDecimal TIMES = BigDecimal.valueOf(1L).divide(QUANTITY, RoundingMode.HALF_UP);

    // 欧盟国家列表
    public static final Set<String> EU_COUNTRIES = new HashSet<>(Arrays.asList(
            "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE",
            "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT",
            "RO", "SK", "SI", "ES", "SE"
    ));

    public static Card testCard() {
        Card card = new Card();
        card.setCardNumber("4929123601798002");
        card.setCardHolderName("YIRAN GU");
        card.setCVV("875");
        card.setExpirationMonth("09");
        card.setExpirationYear("2027");
        return card;
    }

    public static Card testCardNormal() {
        Card card = new Card();
        card.setCardNumber("4596610155666488");
        card.setCardHolderName("YIRAN GU");
        card.setCVV("264");
        card.setExpirationMonth("10");
        card.setExpirationYear("2027");
        return card;
    }

    //不需要3DS的卡
    public static Card testCard2() {
        Card card = new Card();
        card.setCardNumber("4462729692230839");
        card.setCardHolderName("YIRAN GU");
        card.setCVV("389");
        card.setExpirationMonth("09");
        card.setExpirationYear("2026");
        return card;
    }

    //U卡
    public static Card testCard3() {
        Card card = new Card();
        card.setCardNumber("5147548215003113");
        card.setCardHolderName("YIRAN GU");
        card.setCVV("613");
        card.setExpirationMonth("11");
        card.setExpirationYear("2028");
        return card;
    }



    public static UrlDetails defaultUrlDetails(String hots) {
        UrlDetails urlDetails = new UrlDetails();
        urlDetails.setNotificationUrl(hots + NOTIFICATION_URL);
        urlDetails.setSuccessUrl(hots + SUCCESS_URL);
        urlDetails.setFailureUrl(hots + FAILURE_URL);
        return urlDetails;
    }

    public static BrowserDetails defaultBrowserDetails() {
        BrowserDetails browserDetails = new BrowserDetails();
        browserDetails.setAcceptHeader("text/html,application/xhtml+xml");
        browserDetails.setJavaEnabled("TRUE");
        browserDetails.setJavaScriptEnabled("TRUE");
        browserDetails.setLanguage("EN");
        browserDetails.setColorDepth("48");
        browserDetails.setScreenHeight("400");
        browserDetails.setScreenWidth("600");
        browserDetails.setTimeZone("0");
        browserDetails.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47");
        return browserDetails;
    }

    public static BrowserDetails getBrowserDetails(PayOrderVo vo) {
        BrowserDetails details = NuveiCons.defaultBrowserDetails();
        if(vo.getBrowserInfo() != null){
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getAcceptHeader()))details.setAcceptHeader(vo.getBrowserInfo().getAcceptHeader());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getJavaEnabled()))details.setJavaEnabled(vo.getBrowserInfo().getJavaEnabled());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getJavaScriptEnabled()))details.setJavaScriptEnabled(vo.getBrowserInfo().getJavaScriptEnabled());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getLanguage()))details.setLanguage(vo.getBrowserInfo().getLanguage());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getColorDepth()))details.setColorDepth(vo.getBrowserInfo().getColorDepth());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getScreenHeight()))details.setScreenHeight(vo.getBrowserInfo().getScreenHeight());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getScreenWidth()))details.setScreenWidth(vo.getBrowserInfo().getScreenWidth());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getTimeZone()))details.setTimeZone(vo.getBrowserInfo().getTimeZone());
            if(StrUtil.isNotBlank(vo.getBrowserInfo().getUserAgent()))details.setUserAgent(vo.getBrowserInfo().getUserAgent());
        }
        return details;
    }

    public static BrowserDetails fromRequest(HttpServletRequest request) {
        BrowserDetails browserDetails = new BrowserDetails();
        browserDetails.setAcceptHeader(StrUtil.blankToDefault(request.getHeader("Accept"), "text/html,application/xhtml+xml"));
        browserDetails.setJavaEnabled(StrUtil.blankToDefault(request.getParameter("javaEnabled"), "TRUE"));
        browserDetails.setJavaScriptEnabled(StrUtil.blankToDefault(request.getParameter("javaScriptEnabled"), "TRUE"));
        browserDetails.setLanguage(StrUtil.blankToDefault(request.getLocale() != null ? request.getLocale().getLanguage() : null, "EN"));
        browserDetails.setColorDepth(StrUtil.blankToDefault(request.getParameter("colorDepth"), "48"));
        browserDetails.setScreenHeight(StrUtil.blankToDefault(request.getParameter("screenHeight"), "400"));
        browserDetails.setScreenWidth(StrUtil.blankToDefault(request.getParameter("screenWidth"), "600"));
        browserDetails.setTimeZone(StrUtil.blankToDefault(request.getParameter("timeZone"), "0"));
        browserDetails.setUserAgent(StrUtil.blankToDefault(request.getHeader("User-Agent"), "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47)"));
        return browserDetails;
    }

}
