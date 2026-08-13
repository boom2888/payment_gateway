package com.games.nuvei;

import com.games.payment.enums.CardBrandEnums;
import com.safecharge.biz.Safecharge;
import com.safecharge.exception.SafechargeException;
import com.safecharge.util.APIConstants;
import com.safecharge.util.Constants;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class NuveiConfig {
    @Value("${nuvei.merchantId:}")
    private String merchantId;
    @Value("${nuvei.merchantSiteId:}")
    private String merchantSiteId;
    @Value("${nuvei.merchantKey:}")
    private String merchantKey;
    @Value("${nuvei.gatewayPayUrl:}")
    private String gatewayPayUrl;
    @Value("${wynpay.isSignDev:false}")
    private Boolean isSignDev;
    @Value("${nuvei.WynpayNewIntegration.merchantId}")
    private String newMerchantId;
    @Value("${nuvei.WynpayNewIntegration.merchantSiteId}")
    private String newMerchantSiteId;
    @Value("${nuvei.WynpayNewIntegration.merchantKey}")
    private String newMerchantKey;

    public Safecharge init(){
        Safecharge safecharge = new Safecharge();
        try {
            safecharge.initialize(merchantId, merchantSiteId, merchantKey, isSignDev ? APIConstants.INTEGRATION_HOST : APIConstants.PRODUCTION_HOST, Constants.HashAlgorithm.SHA256);
        } catch (SafechargeException e) {
            throw new RuntimeException(e);
        }
        return safecharge;
    }

    public Safecharge init(String cardBrand){
        Safecharge safecharge = new Safecharge();
        String safeMerchantId = merchantId;
        String safeMerchantSiteId = merchantSiteId;
        String safeMerchantKey = merchantKey;

        try {
            if (CardBrandEnums.MASTERCARD.getValue().equalsIgnoreCase(cardBrand)) {
                safeMerchantId = newMerchantId;
                safeMerchantSiteId = newMerchantSiteId;
                safeMerchantKey = newMerchantKey;
            }
            safecharge.initialize(safeMerchantId, safeMerchantSiteId, safeMerchantKey, isSignDev ? APIConstants.INTEGRATION_HOST : APIConstants.PRODUCTION_HOST, Constants.HashAlgorithm.SHA256);
        } catch (SafechargeException e) {
            throw new RuntimeException(e);
        }
        return safecharge;
    }
}
