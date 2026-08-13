package com.games.lhv.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "AccountBalanceResponse")
public class AccountBalanceResponse {
    private String balance;
    private String currency;

    @XmlElement(name = "Balance")
    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }

    @XmlElement(name = "Currency")
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
