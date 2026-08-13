package com.games.pay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.games.pay.vo.bindder.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CreateEntityResDto {
    private Long id;

    private RelationshipVo relationship;

    private String type;

    private String extendedType;

    private IndividualVo individual;

    private LegalEntityVo legalEntity;

    private String email;

    private String phoneNumber;

    private AddressVo address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date clientSince;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date onboardingDate;

    private List<ManagedByVo> managedBy;

    private List<ProvidedServiceVo> providedServices;

    private String notes;

    private LatestNoteVo latestNote;
}
