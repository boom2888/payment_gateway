package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.model.*;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.exception.SafechargeException;
import com.safecharge.util.Constants;

public class PaymentTest {

    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String userTokenId = "230811147";
        String clientUniqueId = "12345";
        String clientRequestId = "1C6CT7V1L";
        String currency = "EUR";
        String amount = "10";
        String relatedTransactionId = "1120000004289428410";
        Constants.TransactionType transactionType = Constants.TransactionType.Sale;
        String userPaymentOptionId = null;

        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("192.168.2.38");

        Card card = new Card();
        card.setCardNumber("4929123601798002");
        card.setCardHolderName("YIRAN GU");
        card.setCVV("875");
        card.setExpirationMonth("09");
        card.setExpirationYear("2027");

        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setUserPaymentOptionId(userPaymentOptionId);
        paymentOption.setCard(card);

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

        UrlDetails urlDetails = new UrlDetails();
        urlDetails.setNotificationUrl("https://www.nuvei.com/notification");

        DynamicDescriptor dynamicDescriptor = new DynamicDescriptor();
        dynamicDescriptor.setMerchantName("Test Merchant");

        SafechargeResponse response = safecharge.payment(null, null, null, paymentOption, null,
                        currency, amount, null, null, deviceDetails, null, null,
                        billingAddress, dynamicDescriptor, null, null, urlDetails, null,
                        null, null, relatedTransactionId, transactionType, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, recipientDetails, null, null,
                        null, null, null);
        System.out.println(response);

    }




}
