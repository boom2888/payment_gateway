package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.response.SafechargeResponse;

public class VoidTransactionTest {

    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String currency = "USD";
        String amount = "3.3";
        String relatedTransactionId = "1120000004291813817";
        //取消之前的付款
        SafechargeResponse response = safecharge.voidTransaction(null, relatedTransactionId, amount, currency, null, null, null, null, null, null, null, null);
        System.out.println(response);
    }


}
