package com.games.api.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@ApiModel("Address Information")
public class AddressInfoVo {

    @ApiModelProperty(value = "Country code", example = "GB", required = true)
    @NotBlank(message = "{country.not.empty}")
    private String country;

    @ApiModelProperty(value = "State code", example = "GB")
    private String state;

    @ApiModelProperty(value = "City", example = "London", required = true)
    @NotBlank(message = "{city.not.empty}")
    private String city;

    @ApiModelProperty(value = "Postal code", example = "SW1A 2AA", required = true)
    @NotBlank(message = "{postal.code.not.empty}")
    private String zip;

    @ApiModelProperty(value = "Street address", example = "Flat 5, 10 Downing Street", required = true)
    @NotBlank(message = "{street.info.not.empty}")
    private String addressLine1;

}
