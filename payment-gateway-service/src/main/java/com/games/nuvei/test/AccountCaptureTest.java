package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.UrlDetails;
import com.safecharge.model.UserDetails;
import com.safecharge.response.SafechargeResponse;

public class AccountCaptureTest {
    public static void main(String[] args) throws SafechargeException {

        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String userTokenId = "230811147";
        String paymentMethod = "apmgw_ACH";
        String currency = "USD";
        String country = "US";
        String language = "en";
        String amount = "200";

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("Alice");
        userDetails.setLastName("Johnson");
        userDetails.setAddress("1234 Elm Street, Apt 5B");
        userDetails.setPhone("+1-202-555-0198");
        userDetails.setZip("10001");
        userDetails.setCity("New York");
        userDetails.setCountry("US");
        userDetails.setState("NY");
        userDetails.setEmail("alice.johnson@example.com");
        userDetails.setLocale("en_US");
        userDetails.setCounty("New York County");
        userDetails.setIdentification("A123456789"); // 假设为护照号或身份证号
        userDetails.setIdentificationType("passport"); // 类型示例：passport、national_id 等
        userDetails.setBirthdate("1990-08-15"); // 格式视 API 要求，常见为 yyyy-MM-dd

        UrlDetails urlDetails = new UrlDetails();
        urlDetails.setSuccessUrl("successUrl");
        urlDetails.setPendingUrl("pendingUrl");
        urlDetails.setFailureUrl("failureUrl");
        urlDetails.setNotificationUrl("notificationUrl");

        SafechargeResponse response = safecharge.accountCapture(null, userTokenId, paymentMethod, currency, country, language, amount, null, null, userDetails, urlDetails);

        System.out.println(response);
    }
}
