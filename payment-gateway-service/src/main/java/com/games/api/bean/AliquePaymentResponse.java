//package com.games.api.bean;
//
//import cn.hutool.core.util.StrUtil;
//import com.games.api.enums.PaymentType;
//import com.games.api.vo.CardInfoVo;
//import com.safecharge.model.Card;
//import com.safecharge.model.ExternalTokenProvider;
//import com.safecharge.model.UserAddress;
//import com.safecharge.response.PaymentResponse;
//import lombok.Data;
//
//@Data
//public class AliquePaymentResponse  {
//
//    private String currency;
//    private String amount;
//    private String clientUniqueId;
//    private String googlePayToken;
//    private String applePayToken;
//    private String paymentType;
//    private String paymentOptionId;
//    private String relatedTransactionId;
//
//    private CardInfoVo cardInfo;
//    private UserAddress billingAddress;
//
//    public Card getCard(){
//        Card card = cardInfo.toCard();
//        if (StrUtil.equals(getPaymentType(), PaymentType.GOOGLE_PAY.getValue())) {
//            ExternalTokenProvider provider =  new ExternalTokenProvider();
//            provider.setExternalTokenProvider("GooglePay");
//            provider.setMobileToken(getGooglePayToken());
//            card.setExternalToken(provider);
//        }
//        if (StrUtil.equals(getPaymentType(), PaymentType.APPLE_PAY.getValue())) {
//            ExternalTokenProvider provider =  new ExternalTokenProvider();
//            provider.setExternalTokenProvider("ApplePay");
//            provider.setMobileToken(getApplePayToken());
//            card.setExternalToken(provider);
//        }
//        return card;
//    }
//
//
//}
