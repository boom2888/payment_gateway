package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.UserDetails;
import com.safecharge.model.UserPaymentOption;
import com.safecharge.response.SafechargeResponse;

public class PayoutTest {
    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String currency = "EUR";
        String amount = "10";
        String userPaymentOptionId = "1120000004288758769";
        //方法用于全额或部分退还先前结算的交易。

        UserPaymentOption upo = new UserPaymentOption();
        upo.setUserPaymentOptionId(userPaymentOptionId);

        UserDetails userDetails = new UserDetails();
        userDetails.setCity("city");
        userDetails.setCountry("country");

        SafechargeResponse response = safecharge.payout(null, null, null, amount, currency, upo, null,
                null, null, null, null, null, null, userDetails, null, null, null);

        System.out.println(response);
    }
}
