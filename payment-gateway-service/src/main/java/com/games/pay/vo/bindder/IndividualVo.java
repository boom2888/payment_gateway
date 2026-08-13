package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndividualVo {
    private NameVo name;
    private String nationality;
    private IdentityDocumentVo identityDocument;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String birthCountryCode;
    private String taxIdentificationNumber;
    private String taxResidenceCountryCode;
}
