package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegalEntityResVo {
    
    private String name;
    
    private String registrationNumber;
    
    private TypeVo type;
    
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date incorporationDate;
    
    private String incorporationCountryCode;
    
    private List<String> operationCountryCodes;
    
    private String vatNumber;
    
    private String accountingYearEnd;
    
    private String websiteUrl;
    
    private List<IndustryVo> industries;
    
    private String industryNotes;
    
    private TrustVo trust;
}
