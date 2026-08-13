package com.games.lhv.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "HeartbeatRequest")
public class HeartbeatRequest {
    private String message;

    @XmlElement(name = "Message")
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
