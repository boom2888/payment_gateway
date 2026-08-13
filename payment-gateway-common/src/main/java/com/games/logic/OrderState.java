//package com.games.logic;
//
///**
// * 业务操作类型
// *
// * @author lor
// */
//public enum OrderState
//{
//    PROCESSING(5, "不包含"),
//    DEPOSIT_DECLINED(6, "包含"),
//    FULLFILLED(8, "商户出"),
//    CLOSE_REFUND(10, "商户出"),
//    CLOSE_DECLINE(13, "商户出"),
//    TDS_PENDING(17, "商户出"),
//    ;
//
//    private final int code;
//    private final String desc;
//
//    OrderState(int code, String desc) {
//        this.code = code;
//        this.desc = desc;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public static OrderState fromCode(Integer code) {
//        if (code == null) {
//            return null;
//        }
//        for (OrderState opt : values()) {
//            if (opt.code == code) {
//                return opt;
//            }
//        }
//        throw new IllegalArgumentException("Unsupported FeeIncludeOption: " + code);
//    }
//}
