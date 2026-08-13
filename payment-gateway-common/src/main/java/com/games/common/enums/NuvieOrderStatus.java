package com.games.common.enums;

import lombok.Getter;

/**
 * 操作状态
 *
 * @author lor
 */
@Getter
public enum NuvieOrderStatus {
    Sales("Sales", "成功"),
    Declined("Declined", "失败"),
    Refund("Credits", "退款"),
    RDR("Pre-Dispute Resolution", "争议"),
    CB("Chargeback", "拒付"),
    CB_CANCELLED("Chargeback Cancelled", "拒付取消"),
    Auth3D("Auth3D", "Auth3D");

    private final String code;
    private final String desc;
    NuvieOrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static NuvieOrderStatus getByCode(String code) {
        for (NuvieOrderStatus status : NuvieOrderStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
