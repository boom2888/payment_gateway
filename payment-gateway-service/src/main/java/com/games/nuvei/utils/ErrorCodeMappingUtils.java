package com.games.nuvei.utils;

import com.games.nuvei.enums.NuveiGwErrorCode;
import com.games.nuvei.enums.PaymentGWErrorCode;

public class ErrorCodeMappingUtils {

    public ErrorCodeMappingUtils() {}

    public static String dealGwErrorCode(Integer gwErrorCode, Integer gwExtendedErrorCode) {
        if (gwErrorCode == NuveiGwErrorCode.FILTER_ERROR.getGwErrorCode()) {
            if (gwExtendedErrorCode.compareTo(NuveiGwErrorCode.FILTER_ERROR.getGwExtendedErrorCode()) > 0) {
                return PaymentGWErrorCode.getDescriptionFromCode(gwExtendedErrorCode);
            }
        }
        return null;
    }

}
