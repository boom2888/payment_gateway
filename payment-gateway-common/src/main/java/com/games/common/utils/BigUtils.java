package com.games.common.utils;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigUtils {



    public static String parseGlobalPower(BigDecimal old){
        BigDecimal max = new BigDecimal(1000);
        int length = 4;
        String[] ends = new String[]{"Bytes", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB", "BiB"};
//        ["Bytes", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB", "BiB"]
        for (int i = 0; i < ends.length; i++) {
            BigDecimal temp = parseGlobalPower(old, i);
            if(temp.abs().compareTo(max) < 0){
                return temp.setScale(length,BigDecimal.ROUND_DOWN).toString() + ends[i];
            }
        }
        return old.setScale(length,BigDecimal.ROUND_HALF_UP).toString();

    }

    public static BigInteger toBig(BigDecimal old, int pow){
        return old.multiply(new BigDecimal(10).pow( pow)).toBigInteger();
    }

    public static BigDecimal parseGlobalPower(BigDecimal old, int pow){
        return old.divide(new BigDecimal(1024).pow( pow), 6, BigDecimal.ROUND_HALF_DOWN);
    }

    public static BigDecimal safeDecimal(Object value){
        double result = 0;
        if(value == null)return BigDecimal.ZERO;
        if(StringUtils.isEmpty(value.toString()))return  BigDecimal.ZERO;
        return new BigDecimal(value.toString());
    }

    public static boolean isBigThanZero(BigDecimal old){
        return old.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isBigThan(BigDecimal value, BigDecimal min){
        return value.compareTo(min) > 0;
    }

    public static boolean isEq(BigDecimal value, BigDecimal min){
        return value.compareTo(min) == 0;
    }

    public static boolean isSmallThanZero(BigDecimal old){
        return old.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isZero(BigDecimal old){
        return old.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断 BigDecimal 是否为整数
     *
     * @param fromAmount 要判断的数值
     * @return 如果是整数返回 true，否则返回 false
     */
    public static boolean isInteger(BigDecimal fromAmount) {
        if (fromAmount == null) {
            return false;
        }
        // 去掉末尾0后再判断 scale 是否为0，即小数位为0
        return NumberUtil.isNumber(fromAmount.stripTrailingZeros().toPlainString()) &&
                fromAmount.stripTrailingZeros().scale() <= 0;
    }

    public static BigDecimal fan(BigDecimal old){
        if(old == null)old = BigDecimal.ZERO;
        return old.multiply(new BigDecimal(-1));
    }

    public static BigDecimal safeBig(BigDecimal old){
        if(old == null)old = BigDecimal.ZERO;
        return old;
    }

    public static BigDecimal formatFee(BigDecimal old){
        return format(old, 8);
    }

    public static BigDecimal formatUsdt(BigDecimal old){
        return format(old, 4);
    }
    public static BigDecimal formatWater(BigDecimal old){
        return format(old, 6);
    }

    public static BigDecimal formatFil(BigDecimal old){
        return format(old, 4);
    }

    public static BigDecimal formatPower(BigDecimal old){
        return format(old, 2);
    }

    public static BigDecimal formatPowerIncome(BigDecimal old){
        return format(old, 4);
    }

    public static BigDecimal format(BigDecimal old, int decimal){
        if(old == null)old = BigDecimal.ZERO;
        return old.setScale(decimal, BigDecimal.ROUND_DOWN);
    }

    public static BigDecimal safeAdd(BigDecimal old, BigDecimal value){
        return safeBig(old).add(value);
    }
    public static BigDecimal safeSubtract(BigDecimal old, BigDecimal value){
        return safeBig(old).subtract(safeBig(value));
    }
    public static BigDecimal safeMultiply(BigDecimal old, BigDecimal value){
        return safeBig(old).multiply(safeBig(value));
    }

    public static BigDecimal parseFromString(String amount){
        BigDecimal amountData = new BigDecimal(amount).divide(new BigDecimal(1000000000000000000L));
        return amountData;
    }
    public static BigDecimal parse6FromString(String amount){
        BigDecimal amountData = new BigDecimal(amount).divide(new BigDecimal(1000000L));
        return amountData;
    }

    public static BigDecimal parseFromBig(BigInteger amount, int decimal){
        BigDecimal amountData = new BigDecimal(amount).divide(new BigDecimal(10).pow(decimal));
        return amountData;
    }

    public static BigDecimal parseFromBig(BigInteger amount){
        BigDecimal amountData = new BigDecimal(amount).divide(new BigDecimal(1000000000000000000L));
        return amountData;
    }

    public static String calcSuccessRate(int succOrderTotal, int failOrderTotal) {
        int total = succOrderTotal + failOrderTotal;
        if (total == 0) {
            return "0.00%";
        }
        BigDecimal succ = BigDecimal.valueOf(succOrderTotal);
        BigDecimal rate = succ
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP) // 保留4位，避免精度丢失
                .multiply(BigDecimal.valueOf(100)); // 转换成百分比

        return rate.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
    }

//    BigInteger amountData = new BigDecimal(amount).multiply(new BigDecimal(1000000000000000000L)).toBigInteger();
}
