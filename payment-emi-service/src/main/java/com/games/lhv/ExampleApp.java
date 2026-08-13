package com.games.lhv;

import com.games.lhv.model.HeartbeatResponse;
import com.games.lhv.model.PaymentRequest;
import com.games.lhv.model.AccountBalanceRequest;
import com.games.lhv.model.AccountBalanceResponse;

public class ExampleApp {
    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
        // 或备选：
        System.setProperty("com.sun.xml.bind.v2.runtime.reflect.opt.OptimizedAccessorFactory.noOptimize", "true");

        String baseUrl = "https://connect.prelive.lhv.eu"; // sandbox or production
//        String baseUrl = " https://connect.lhv.eu"; // sandbox or production
        String clientCode = "YOUR_CLIENT_CODE";
        String clientCountry = "EE";

        LhvClient client = new LhvClient(baseUrl, clientCode, clientCountry);
        LhvService service = new LhvService(client);

        System.out.println("== Heartbeat ==");
        try {
            HeartbeatResponse resp = service.heartbeat();
            System.out.println("Heartbeat time: " + resp.getTimeStamp());
        } catch (Exception e) {
            System.err.println("Heartbeat failed: " + e.getMessage());
        }

        System.out.println("== Initiate Payment (example) ==");
        PaymentRequest preq = new PaymentRequest();
        preq.setPaymentId("p-001");
        preq.setAmount("10.00");
        preq.setCurrency("EUR");
        try {
            service.initiatePayment(preq);
            System.out.println("Payment request sent.");
        } catch (Exception e) {
            System.err.println("Payment failed: " + e.getMessage());
        }

        System.out.println("== Account Balance ==");
        AccountBalanceRequest balReq = new AccountBalanceRequest();
        balReq.setAccountId("acc-001");
        try {
            AccountBalanceResponse balResp = service.accountBalance(balReq);
            System.out.println("Balance: " + balResp.getBalance() + " " + balResp.getCurrency());
        } catch (Exception e) {
            System.err.println("Balance failed: " + e.getMessage());
        }
    }
}
