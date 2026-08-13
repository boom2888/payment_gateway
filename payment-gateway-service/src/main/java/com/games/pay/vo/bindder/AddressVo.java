package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressVo {
    
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String postalCode;
    
    private String countryCode;
}
