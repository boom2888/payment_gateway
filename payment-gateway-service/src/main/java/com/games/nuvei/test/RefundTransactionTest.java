package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.response.SafechargeResponse;

public class RefundTransactionTest {

    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        String currency = "EUR";
        String amount = "2";
        String relatedTransactionId = "2130000006001506757";
        //方法用于全额或部分退还先前结算的交易。
        SafechargeResponse response = safecharge.refundTransaction(null, null, null, amount, null, null, currency, null, null, null, relatedTransactionId, null, null, null, null);
        System.out.println(response);
    }

}
