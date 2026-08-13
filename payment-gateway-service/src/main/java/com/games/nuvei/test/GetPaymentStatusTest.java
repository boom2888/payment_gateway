package com.games.nuvei.test;

import com.games.nuvei.NuveiConfig;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.UserDetails;
import com.safecharge.model.UserPaymentOption;
import com.safecharge.response.GetPaymentStatusResponse;
import com.safecharge.response.GetPayoutStatusResponse;

public class GetPaymentStatusTest {

    public static void main(String[] args) throws SafechargeException {
        NuveiConfig config = new NuveiConfig();
        Safecharge safecharge = config.init();

        GetPaymentStatusResponse response = safecharge.getPaymentStatus();
        GetPayoutStatusResponse response2 = safecharge.getPayoutStatus("");

        System.out.println(response);
    }
}
