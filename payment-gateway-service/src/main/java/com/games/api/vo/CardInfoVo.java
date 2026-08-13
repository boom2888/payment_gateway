package com.games.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@ApiModel("Card Information")
public class CardInfoVo {
    @ApiModelProperty(value = "Card number (no spaces or separators)", example = "5545060700002228", required = true)
    @NotBlank(message = "{bank.card.number.not.empty}")
    private String cardNumber;

    @ApiModelProperty(value = "Card fingerprint (system generated)", hidden = true)
    private String cardFinger;

    @ApiModelProperty(value = "Cardholder last name", example = "CL-", required = true)
    @NotBlank(message = "{cardholder.last.name.not.empty}")
    private String firstName;

    @ApiModelProperty(value = "Cardholder first name", example = "BRW2", required = true)
    @NotBlank(message = "{cardholder.first.name.not.empty}")
    private String lastName;

    @ApiModelProperty(value = "Card expiry month (MM)", example = "12", required = true)
    @NotBlank(message = "{expiry.month.not.empty}")
    private String expiryMonth;

    @ApiModelProperty(value = "Card expiry year (YYYY)", example = "2027", required = true)
    @NotBlank(message = "{expiry.year.not.empty}")
    private String expiryYear;

    @ApiModelProperty(value = "Card security code (CVV/CVC)", example = "123", required = true)
    @NotBlank(message = "{cvv.not.empty}")
    private String cvv;

    @ApiModelProperty(value = "Card-bound email", example = "123", required = true)
    @NotBlank(message = "{card.binding.email.not.empty}")
    private String email;

    // ========== Shift4 AFT fields ==========
    
    @ApiModelProperty(value = "Source of funds (s18). 01-Credit Card,02-Debit Card,03-Prepaid Card,04-Cash,05-Debit/Deposit Account,06-Credit Account,25-Mobile Money Account", example = "02")
    @Pattern(regexp = "^(01|02|03|04|05|06|25)?$", message = "Invalid source of funds value. Supported values: 01,02,03,04,05,06,25")
    private String sourceOfFunds;

    @ApiModelProperty(value = "Sender date of birth (s19), format MMDDYYYY", example = "01151990")
    @Pattern(regexp = "^\\d{8}?$", message = "Date of birth must be in MMDDYYYY format")
    private String dateOfBirth;

}
