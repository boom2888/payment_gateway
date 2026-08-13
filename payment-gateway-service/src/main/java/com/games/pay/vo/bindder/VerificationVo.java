package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationVo {
    private String id;
    private String type;
    private String name;
    private String status;
    private Boolean pending;
}

