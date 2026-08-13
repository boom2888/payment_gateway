package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.model.*;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.exception.SafechargeException;
import com.safecharge.util.Constants;

public class PaymentFullParamsTest {
    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String userTokenId = "230811147";
        String clientUniqueId = "client-123456";
        String clientRequestId = "req-123456";
        String currency = "USD";
        String amount = "100.00";

        // PaymentOption (with Card + 3DS)
        Card card = new Card();
        card.setCardNumber("4000027891380961");
        card.setCardHolderName("John Smith");
        card.setCVV("217");
        card.setExpirationMonth("12");
        card.setExpirationYear("2030");

        BrowserDetails browserDetails = new BrowserDetails();
        browserDetails.setAcceptHeader("text/html");
        browserDetails.setIp("192.168.1.11");
        browserDetails.setJavaEnabled("TRUE");
        browserDetails.setJavaScriptEnabled("TRUE");
        browserDetails.setLanguage("EN");
        browserDetails.setColorDepth("24");
        browserDetails.setScreenHeight("1080");
        browserDetails.setScreenWidth("1920");
        browserDetails.setTimeZone("0");
        browserDetails.setUserAgent("Mozilla/5.0");

        ThreeD threeD = new ThreeD();
        threeD.setBrowserDetails(browserDetails);
        threeD.setVersion("2");
        threeD.setNotificationURL("https://yourdomain.com/notify");
        threeD.setMerchantURL("https://yourdomain.com/merchant");
        threeD.setPlatformType("02");

        card.setThreeD(threeD);

        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setCard(card);

        Integer isRebilling = 0;

        AmountDetails amountDetails = new AmountDetails();
//        amountDetails.setShipping("10");
//        amountDetails.setSubtotal("90");

//        List<Item> items = Arrays.asList(new Item("item1", "item desc", "1", "90"));

        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("192.168.1.100");

        RestApiUserDetails userDetails = new RestApiUserDetails();
        userDetails.setFirstName("John");
        userDetails.setLastName("Smith");
        userDetails.setEmail("john@example.com");

        UserAddress shippingAddress = new UserAddress();
        shippingAddress.setFirstName("John");
        shippingAddress.setLastName("Smith");
        shippingAddress.setEmail("john@example.com");
        shippingAddress.setCountry("US");

        UserAddress billingAddress = shippingAddress;

        DynamicDescriptor dynamicDescriptor = new DynamicDescriptor();
        dynamicDescriptor.setMerchantName("Demo Merchant");
        dynamicDescriptor.setMerchantPhone("123456789");

        MerchantDetails merchantDetails = new MerchantDetails();
        merchantDetails.setCustomField1("field1");

        Addendums addendums = new Addendums();
//        addendums.setAirlineData(null); // 如果需要传航空票务等数据可补充

        UrlDetails urlDetails = new UrlDetails();
        urlDetails.setSuccessUrl("https://yourdomain.com/success");
        urlDetails.setFailureUrl("https://yourdomain.com/failure");
        urlDetails.setPendingUrl("https://yourdomain.com/pending");

        String customSiteName = "YourSite";
        String productId = "product-001";
        String customData = "any-custom-data";
        String relatedTransactionId = null;

        Constants.TransactionType transactionType = Constants.TransactionType.Sale;
        Boolean autoPayment3D = false;
        String isMoto = "0";

        SubMerchant subMerchant = new SubMerchant();
        subMerchant.setId("sub123");

        String rebillingType = null;
        String authenticationOnlyType = null;
        String userId = "user123";

        ExternalSchemeDetails externalSchemeDetails = new ExternalSchemeDetails();
//        externalSchemeDetails.setThreeDSVersion("2.1.0");

        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setOriginalAmount("100.00");
        currencyConversion.setOriginalCurrency("USD");

        String isPartialApproval = "0";
        String paymentFlow = null;
        String redirectFlowUITheme = null;
        String aftOverride = null;

        RecipientDetails recipientDetails = new RecipientDetails();
        recipientDetails.setFirstName("Recipient");
        recipientDetails.setLastName("Demo");

        CompanyDetails companyDetails = new CompanyDetails();
//        companyDetails.setCompanyName("Demo Co.");

        ShippingTrackingDetails shippingTrackingDetails = new ShippingTrackingDetails();
//        shippingTrackingDetails.setTrackingNumber("TRK123456");

        String cvvNotUsed = null;
        String serviceDueDate = null;
        String digitalAssetType = null;

        SafechargeResponse response = safecharge.payment(
                userTokenId, clientUniqueId, clientRequestId, paymentOption, isRebilling,
                currency, amount, amountDetails, null, deviceDetails, userDetails, shippingAddress,
                billingAddress, dynamicDescriptor, merchantDetails, addendums, urlDetails, customSiteName,
                productId, customData, relatedTransactionId, transactionType, autoPayment3D, isMoto, subMerchant,
                rebillingType, authenticationOnlyType, userId, externalSchemeDetails, currencyConversion,
                isPartialApproval, paymentFlow, redirectFlowUITheme, aftOverride, recipientDetails,
                companyDetails, shippingTrackingDetails, cvvNotUsed, serviceDueDate, digitalAssetType
        );

        System.out.println(response);
    }
}