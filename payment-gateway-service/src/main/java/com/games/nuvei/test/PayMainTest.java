package com.games.nuvei.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.games.nuvei.NuveiConfig;
import com.games.nuvei.NuveiCons;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.*;
import com.safecharge.response.InitPaymentResponse;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.util.Constants;

public class PayMainTest {

    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String userTokenId = "230811147";
        String clientUniqueId = "12345";
        String clientRequestId = "1C6CT7V1L";


        String currency = "USD";
        String amount = "3.3";

        Constants.TransactionType transactionType = Constants.TransactionType.Sale;

        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("192.168.2.38");

        Card card = NuveiCons.testCard2();

        UserAddress billingAddress = new UserAddress();
        billingAddress.setFirstName("YIRAN");
        billingAddress.setLastName("GU");
        billingAddress.setAddress("1234 Elm Street, Apt 5B");
        billingAddress.setCity("New York");
        billingAddress.setCountry("US");
        billingAddress.setEmail("boom@gmail.com");

        RecipientDetails recipientDetails = new RecipientDetails();
        recipientDetails.setFirstName("YIRAN");
        recipientDetails.setLastName("GU");


        DynamicDescriptor dynamicDescriptor = new DynamicDescriptor();
        dynamicDescriptor.setMerchantName("Test Merchant");

        InitPaymentCard cardInit = BeanUtil.copyProperties(card, InitPaymentCard.class);
        InitPaymentPaymentOption initPaymentOption = new InitPaymentPaymentOption();
        initPaymentOption.setCard(cardInit);

        InitPaymentResponse response = safecharge.initPayment(null, null, null, currency, amount,
                deviceDetails, initPaymentOption, null,
                null, null, null, null, recipientDetails, null);

        System.out.println(response);
        String version = response.getPaymentOption().getCard().getThreeD().getVersion();

        String relatedTransactionId = response.getTransactionId();

        if(StrUtil.isNotBlank(version)){
            ThreeD threeD = new ThreeD();
            threeD.setMethodCompletionInd("U");
            threeD.setPlatformType("02");
            threeD.setVersion(version);
            threeD.setNotificationURL(config.getGatewayPayUrl() + NuveiCons.NOTIFICATION_URL);
            threeD.setMerchantURL(config.getGatewayPayUrl());
            V2AdditionalParams additionalParams = new V2AdditionalParams();
            additionalParams.setChallengePreference("01");
            additionalParams.setChallengeWindowSize("05");

            BrowserDetails browserDetails = NuveiCons.defaultBrowserDetails();
            browserDetails.setIp(deviceDetails.getIpAddress());
            threeD.setBrowserDetails(browserDetails);

            threeD.setV2AdditionalParams(additionalParams);
            card.setThreeD(threeD);
        }

        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setUserPaymentOptionId(null);
        paymentOption.setCard(card);




        SafechargeResponse payResponse = safecharge.payment(null, null, null, paymentOption, null,
                currency, amount, null, null, deviceDetails, null, null,
                billingAddress, dynamicDescriptor, null, null, NuveiCons.defaultUrlDetails("https://api.deepay.ai"), null,
                null, null, relatedTransactionId, transactionType, null, null, null,
                null, null, null, null, null,
                null, null, null, null, recipientDetails, null, null,
                null, null, null);
        System.out.println(payResponse);

    }
    

}
