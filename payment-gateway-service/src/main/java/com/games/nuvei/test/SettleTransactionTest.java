package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.response.SafechargeResponse;

public class SettleTransactionTest {
    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String currency = "EUR";
        String amount = "10";
        String relatedTransactionId = "1120000004288758769";
        String authCode = "038000";

        SafechargeResponse response = safecharge.settleTransaction(null, null, null, null, null, null, amount, authCode, null, null, currency, null, null, relatedTransactionId, null, null);
        System.out.println(response);
    }
}
