package com.games.lhv.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "AccountBalanceRequest")
public class AccountBalanceRequest {
    private String accountId;

    @XmlElement(name = "AccountId")
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
}
