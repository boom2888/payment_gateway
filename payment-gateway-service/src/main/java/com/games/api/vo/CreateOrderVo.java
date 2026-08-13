package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.games.api.enums.OrderType;
import com.games.common.exception.BusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Data
@ApiModel("Create Order Request")
public class CreateOrderVo extends ApiBaseReq{

    public void check() {
        // Validate that orderType is in the supported enum set
        List<String> allowedOrderTypes = Arrays.asList(OrderType.ON_RAMP.getName(),
                OrderType.NFT_ON_RAMP.getName(), OrderType.NFT_ON_RAMP.getName());
        if (!allowedOrderTypes.contains(orderType)) {
            throw BusinessException.api("order.type.not.supported");
        }

        // Validate orderAmount format and range
        try {
            if (orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw BusinessException.api("order.amount.must.greater.than.zero");
            }
        } catch (NumberFormatException e) {
            throw BusinessException.api("order.amount.format.error");
        }

        // Validate currency format as 3 uppercase letters
        if (!currency.matches("^[A-Z]{3}$")) {
            throw BusinessException.api("currency.code.format.error");
        }

        // Validate customerDob format as YYYY-MM-DD
        if (!customerDob.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw BusinessException.api("customer.birth.date.format.error");
        }

        // Validate customerCountry as a 2-letter uppercase country code
        if (!customerCountry.matches("^[A-Z]{2}$")) {
            throw BusinessException.api("country.code.format.error");
        }
    }

    @NotNull(message = "{order.type.not.empty}")
    @ApiModelProperty(value = "Order type, e.g. ON_RAMP, NFT_ON_RAMP, TOKEN_ON_RAMP", required = true, example = "ON_RAMP")
    private String orderType;

    @NotBlank(message = "{merchant.order.id.not.empty}")
    @ApiModelProperty(value = "Merchant order ID", required = true, example = "merchant_order_id_1009")
    private String ssoOrderId;

    @NotNull(message = "{order.amount.not.empty}")
    @DecimalMin(value = "0.01", message = "Order amount must be greater than 0")
    @ApiModelProperty(value = "Order amount", required = true, example = "15")
    private BigDecimal orderAmount;

    // ==== Currency fields ====

    @NotBlank(message = "{currency.code.not.empty}")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code format, must be 3 uppercase letters")
    @ApiModelProperty(value = "Fiat currency code (3 uppercase letters)", required = true, example = "USD")
    private String currency;

    @NotBlank(message = "{crypto.currency.not.empty}")
    @Pattern(regexp = "^[A-Z]{3,6}$", message = "Invalid cryptocurrency format, must be uppercase letters")
    @ApiModelProperty(value = "Cryptocurrency code (uppercase)", required = true, example = "USDT")
    private String cryptoCurrency;

    @ApiModelProperty(value = "Product name", example = "Good")
    private String productName;

    // ==== Customer fields ====
    @NotBlank(message = "{merchant.user.id.not.empty}")
    @ApiModelProperty(value = "Merchant user ID", required = true, example = "merchant_user_id_1001")
    private String customerId;

    @ApiModelProperty(value = "Customer wallet address", example = "0x388C818CA8B9251b393131C08a736A67ccB19297")
    private String customerWallet;

    @NotBlank(message = "{customer.email.not.empty}")
    @Email(message = "Invalid customer email format")
    @ApiModelProperty(value = "Customer email", required = true, example = "boom@gmail.com")
    private String customerEmail;

    @NotBlank(message = "{customer.first.name.not.empty}")
    @ApiModelProperty(value = "Customer first name", required = true, example = "Jecson")
    private String customerFirstName;

    @NotBlank(message = "{customer.last.name.not.empty}")
    @ApiModelProperty(value = "Customer last name", required = true, example = "Tom")
    private String customerLastName;

    @NotBlank(message = "{customer.birth.date.not.empty}")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid birth date format, should be YYYY-MM-DD")
    @ApiModelProperty(value = "Customer date of birth", required = true, example = "1990-01-01")
    private String customerDob;

    @NotBlank(message = "{customer.address.not.empty}")
    @ApiModelProperty(value = "Customer address", required = true, example = "350 5th Ave")
    private String customerAddress;

    @NotBlank(message = "{customer.city.not.empty}")
    @ApiModelProperty(value = "Customer city", required = true, example = "New York")
    private String customerCity;

    @NotBlank(message = "{customer.country.not.empty}")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Customer country must be a 2-letter uppercase country code (e.g. US)")
    @ApiModelProperty(value = "Customer country (2 uppercase-letter code)", required = true, example = "US")
    private String customerCountry;

//    @NotBlank(message = "客户州不能为空")
    @ApiModelProperty(value = "Customer state", required = true, example = "NY")
    private String customerState;

    @NotBlank(message = "{customer.postal.code.not.empty}")
    @ApiModelProperty(value = "Customer ZIP code", required = true, example = "10118")
    private String customerZip;

    @ApiModelProperty(value = "Fee inclusion mode (EXCLUDE: excluded, INCLUDE: included, MERCHANT: merchant pays)", required = true, example = "INCLUDE")
    private String include;

    /**
     * If omitted, returns JSON; if set to 1, returns redirect result
     */
    private Integer createType;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return ssoOrderId;
    }
}
