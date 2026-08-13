package com.games.lhv.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "PaymentRequest")
public class PaymentRequest {
    private String paymentId;
    private String amount;
    private String currency;

    @XmlElement(name = "PaymentId")
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    @XmlElement(name = "Amount")
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    @XmlElement(name = "Currency")
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
