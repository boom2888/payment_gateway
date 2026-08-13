package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrustVo {
    private String trustType;
    private String beneficialInterestType;
    private String revocabilityType;
    private Integer periodYears;
    private String properLawCountryCode;
}
