package com.games.lhv.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "PaymentResponse")
public class PaymentResponse {
    private String status;
    private String transactionId;

    @XmlElement(name = "Status")
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @XmlElement(name = "TransactionId")
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
