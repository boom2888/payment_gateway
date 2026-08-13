package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.*;
import com.safecharge.response.InitPaymentResponse;
import com.safecharge.response.SafechargeResponse;

public class InitPaymentTest {
    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String userTokenId = "230811147";
        String clientUniqueId = "12345";
        String clientRequestId = "1C6CT7V1L";
        String currency = "EUR";
        String amount = "10";

        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("192.168.2.38");

//        InitPaymentThreeD threeD = new InitPaymentThreeD();
//        threeD.setMethodNotificationUrl("<methodNotificationURL>");

        InitPaymentCard card = new InitPaymentCard();
        card.setCardNumber("4929123601798002");
        card.setCardHolderName("YIRAN GU");
        card.setCVV("875");
        card.setExpirationMonth("09");
        card.setExpirationYear("2027");
//        card.setThreeD(threeD);

        InitPaymentPaymentOption initPaymentOption = new InitPaymentPaymentOption();
        initPaymentOption.setCard(card);

        String aftOverride = null;
        RecipientDetails recipientDetails  = new RecipientDetails();
        recipientDetails.setFirstName("YIRAN");
        recipientDetails.setLastName("GU");
        String relatedTransactionId = null;

        InitPaymentResponse response = safecharge.initPayment(null, null, null, currency, amount,
                deviceDetails, initPaymentOption, null,
                null, null, null, aftOverride, recipientDetails, relatedTransactionId);

        System.out.println(response);
        String version = response.getPaymentOption().getCard().getThreeD().getVersion();

        System.out.println("111");

    }
}
