package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.response.SafechargeResponse;

public class GetCardDetailsTest {
    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String cardNumber = "4000027891380961";
        SafechargeResponse response = safecharge.getCardDetails(null, null, cardNumber);

        System.out.println(response);

    }
}
