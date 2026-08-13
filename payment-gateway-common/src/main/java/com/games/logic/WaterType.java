package com.games.logic;

/**
 * 业务操作类型
 *
 * @author lor
 */
public enum WaterType {
    /**
     * 未知
     */
    UNKOWN("未知"),

    /**
     * 充值
     */
    CHARGE("充值"),

    /**
     * 提现
     */
    WITHDRAW("提现"),

    /**
     * 管理费奖励
     */
    BACK_ADMIN("管理费奖励"),

    /**
     * 邀请奖励
     */
    BACK_INVITE("邀请奖励"),

    /**
     * 红包奖励
     */
    BACK_COUPON("红包奖励"),

    /**
     * 支付订单
     */
    PAY_ORDER("支付订单"),

    /**
     * 支付GAS
     */
    PAY_GAS("支付GAS"),

    /**
     * 冻结质押币
     */
    LOCK_PLEDGE("冻结质押币"),

    /**
     * 退还质押币
     */
    UNLOCK_PLEDGE("退还质押币"),

    /**
     * 挖矿收益
     */
    DRUG_INCOME("挖矿收益"),

    /**
     * 挖矿管理费
     */
    DRUG_ADMIN("挖矿管理费"),

    /**
     * 平台管理费
     */
    PLANT_ADMIN("平台管理费"),

    /**
     * 转入质押账户
     */
    PLEDGE_IN("转入质押账户"),

    /**
     * 转出质押账户
     */
    PLEDGE_OUT("转出质押账户");

    private String desc;
    WaterType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static String format(String code) {
        try{
            WaterType type = WaterType.valueOf(code);
            if(type != null)return type.getDesc();
        }catch (Exception e){}
        return UNKOWN.getDesc();
    }
}
