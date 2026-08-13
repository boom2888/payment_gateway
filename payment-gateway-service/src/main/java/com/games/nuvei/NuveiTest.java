package com.games.nuvei;

//import cn.hutool.core.collection.CollUtil;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.Card;
import com.safecharge.model.DeviceDetails;
import com.safecharge.model.PaymentOption;
import com.safecharge.model.UserAddress;
import com.safecharge.response.DccDetailsResponse;
import com.safecharge.response.McpRatesResponse;
import com.safecharge.response.PaymentResponse;
import com.safecharge.response.SafechargeResponse;

public class NuveiTest {

    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();
//        safecharge.openOrder()
//        safecharge.initPayment();
//        String clientUniqueId = System.currentTimeMillis()+"";
//        String clientRequestId = System.currentTimeMillis()+"";



//        String userTokenId = "<unique customer identifier in merchant system>";
//        String clientRequestId = "<unique request ID in merchant system>";
//        String clientUniqueId = "<unique transaction ID in merchant system>";
//        String currency = "USD";
//        String amount = "200";
//        DeviceDetails deviceDetails = new DeviceDetails();
//        deviceDetails.setIpAddress("<customer's IP address>");
//
//        PaymentOption paymentOption = new PaymentOption();
//        Card card = new Card();
//        card.setCardNumber("4000027891380961");
//        card.setCardHolderName("John Smith");
//        card.setCVV("217");
//        card.setExpirationMonth("12");
//        card.setExpirationYear("2030");
//        paymentOption.setCard(card);
//
//        UserAddress billingAddress = new UserAddress();
//        billingAddress.setEmail("john.smith@email.com");
//        billingAddress.setCountry("US");
//        SafechargeResponse response = safecharge.payment(userTokenId, clientUniqueId, clientRequestId,
//                paymentOption, null, currency, amount, null, null, deviceDetails, null, null, billingAddress, null,
//                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
//
//
//
//        PaymentResponse paymentResponse = safecharge.payment(userTokenId, clientUniqueId, clientRequestId, paymentOption, null, currency,
//                amount, null, deviceDetails, userDetails, shippingAddress, billingAddress,
//                dynamicDescriptor, merchantDetails, null, urlDetails, null, null, null,
//                null, null, false, null, subMerchant);

        //商家可以使用此方法指定源货币，并检索当前按货币和付款方式计算的汇率（含加价）以及时间戳。时间戳会保留在系统中，与相关汇率矩阵快照相邻。
//        McpRatesResponse ratesResponse = safecharge.getMcpRates(null, null, "EUR", CollUtil.toList(new String[]{ "USD"}), CollUtil.toList(new String[]{ "cc_card"}));
//        ratesResponse.getRates().forEach(System.out::println);
//        System.out.println(CollUtil.join(ratesResponse.getRates(), ", "));


//        String clientRequestId = "1C6CT7V1L";
//        String clientUniqueId = "12345";
//        String originalCurrency = "USD";
//        String currency = "EUR";
//        String originalAmount = "200";
//        String cardNumber = "4000027891380961";
//        DccDetailsResponse response = safecharge.getDccDetails(clientUniqueId, clientRequestId, cardNumber, null, originalAmount, originalCurrency, currency, null);

        System.out.println("1111");

    }

}
