package com.games.lhv;

import com.games.lhv.model.HeartbeatRequest;
import com.games.lhv.model.HeartbeatResponse;
import com.games.lhv.model.PaymentRequest;
import com.games.lhv.model.PaymentResponse;
import com.games.lhv.model.AccountBalanceRequest;
import com.games.lhv.model.AccountBalanceResponse;

/**
 * High level service wrapper for common LHV Connect operations.
 */
public class LhvService {
    private final LhvClient client;

    public LhvService(LhvClient client) {
        this.client = client;
    }

    public HeartbeatResponse heartbeat() throws Exception {
        HeartbeatRequest req = new HeartbeatRequest();
        req.setMessage("ping");
        String respXml = client.postXml("/heartbeat", req);
        return client.fromXml(respXml, HeartbeatResponse.class);
    }

    public PaymentResponse initiatePayment(PaymentRequest request) throws Exception {
        String respXml = client.postXml("/payment", request);
        return client.fromXml(respXml, PaymentResponse.class);
    }

    public AccountBalanceResponse accountBalance(AccountBalanceRequest request) throws Exception {
        String respXml = client.postXml("/account-balance", request);
        return client.fromXml(respXml, AccountBalanceResponse.class);
    }
}
