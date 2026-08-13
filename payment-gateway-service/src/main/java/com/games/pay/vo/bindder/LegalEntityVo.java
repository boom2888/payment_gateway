package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegalEntityVo {
    @NotNull(message = "{registration.country.code.not.empty}")
    private String incorporationCountryCode;

    @NotNull(message = "{entity.type.not.empty}")
    @Valid
    private TypeVo type;

    @NotNull(message = "{registration.number.not.empty}")
    private String registrationNumber;

    @NotNull(message = "{entity.name.not.empty}")
    private String name;

    private String accountingYearEnd;
}
