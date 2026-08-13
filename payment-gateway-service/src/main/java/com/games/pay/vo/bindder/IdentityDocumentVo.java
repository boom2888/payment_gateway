package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentityDocumentVo {
    private Long id;
    private String type;
    private String number;
    private String countryOfIssue;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private List<DocumentPhotoVo> photos;
    private String status;
    private Boolean isExpired;
    private List<ErrorMessageVo> errorMessages;
}
