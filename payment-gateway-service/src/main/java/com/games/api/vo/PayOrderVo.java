package com.games.api.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.games.api.enums.PaymentType;
import com.games.common.exception.BusinessException;
import com.games.common.utils.ip.IpUtils;
import com.safecharge.model.Card;
import com.safecharge.model.DeviceDetails;
import com.safecharge.model.ExternalTokenProvider;
import com.safecharge.model.UserAddress;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@ApiModel("Pay Order Request")
public class PayOrderVo extends ApiBaseReq{

    @ApiModelProperty(value = "Payment method. Fixed values: card, google_pay, apple_pay", required = true, example = "card")
    @NotBlank(message = "{payment.method.not.empty}")
    private String paymentType;

    @ApiModelProperty(value = "Unique order ID on our platform (use this value for subsequent order operations)", example = "8100", required = true)
    private String orderNo;

    @ApiModelProperty(value = "Card information (required when paymentType is card)")
    @NotNull(message = "Card info cannot be empty (for card payments)")
    private CardInfoVo cardInfo;

    @ApiModelProperty(value = "Cardholder address information (required when paymentType is card)")
    @NotNull(message = "{cardholder.address.info.not.empty}")
    private AddressInfoVo addressInfo;

    @ApiModelProperty(value = "Google Pay token (required when paymentType is google_pay)", example = "")
    private String googlePayToken;

    @ApiModelProperty(value = "Apple Pay token (required when paymentType is apple_pay)", example = "")
    private String applePayToken;

    @ApiModelProperty(value = "Browser information")
    private BrowserInfoVo browserInfo;

    @ApiModelProperty(value = "Device details")
    private DeviceDetailsVo deviceDetail;

//    @ApiModelProperty(value = "Related transaction ID")
//    private String relatedTransactionId;

    @Override
    public void check(boolean isApi) {
        super.check(isApi);
        if (deviceDetail == null) {
            deviceDetail = new DeviceDetailsVo();
        }
        if(isApi){
            // API integration requires IP
            if (StrUtil.isBlank(deviceDetail.getIpAddress())) {
                throw new BusinessException("ip address null error");
            }
        }else{
            String ip = IpUtils.getIp();
            deviceDetail.setIpAddress(ip);
        }

        System.out.println(StrUtil.format("Order:{}, payment client IP:{}", orderNo, deviceDetail.getIpAddress()));
        if (StrUtil.isBlank(orderNo)) {
            throw new BusinessException("orderNo null error");
        }
        if (StrUtil.isBlank(paymentType)) {
            throw new BusinessException("paymentType null error");
        }
        if (cardInfo == null) {
            throw new BusinessException("cardInfo null error");
        }
        if (addressInfo == null) {
            throw new BusinessException("addressInfo null error");
        }
        if (deviceDetail == null) {
            throw new BusinessException("deviceDetail null error");
        }
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return orderNo;
    }

    public UserAddress toUserAddress(String userEmail, String phone) {
        UserAddress billingAddress = BeanUtil.copyProperties(addressInfo, UserAddress.class);
        billingAddress.setFirstName(cardInfo.getFirstName());
        billingAddress.setLastName(cardInfo.getLastName());
        billingAddress.setAddress(addressInfo.getAddressLine1());
        billingAddress.setEmail(userEmail);
        billingAddress.setPhone(phone);
        return billingAddress;
    }

    public Card toCard(){
        Card card = BeanUtil.copyProperties(cardInfo, Card.class);
        String cardHolderName = cardInfo.getFirstName() + " " + cardInfo.getLastName();
        card.setCardHolderName(cardHolderName);
        card.setExpirationMonth(cardInfo.getExpiryMonth());
        card.setExpirationYear(cardInfo.getExpiryYear());
        card.setCVV(cardInfo.getCvv());

        if (StrUtil.equals(getPaymentType(), PaymentType.GOOGLE_PAY.getValue())) {
            ExternalTokenProvider provider =  new ExternalTokenProvider();
            provider.setExternalTokenProvider("GooglePay");
            provider.setMobileToken(googlePayToken);
            card.setExternalToken(provider);
        }
        if (StrUtil.equals(getPaymentType(), PaymentType.APPLE_PAY.getValue())) {
            ExternalTokenProvider provider =  new ExternalTokenProvider();
            provider.setExternalTokenProvider("ApplePay");
            provider.setMobileToken(applePayToken);
            card.setExternalToken(provider);
        }

        return card;
    }

    public DeviceDetails toDeviceDetails(){
        DeviceDetails details = BeanUtil.copyProperties(deviceDetail, DeviceDetails.class);
//        if(details == null){
//            details = new DeviceDetails();
//        }
//        if(deviceDetail == null || StrUtil.isBlank(deviceDetail.getIpAddress())){
//            String ip = IpUtils.getIp();
//            details.setIpAddress(ip);
//        }
        return details;
    }

}
