package com.games.payment.enums;

import cn.hutool.core.collection.ListUtil;
import lombok.Getter;

import java.util.List;

@Getter
public enum ShopStatus {

    WAIT("WAIT", "待审核"),

    OK("OK", "正常"),

    FAIL("FAIL", "审核失败"),

    LOCK("LOCK", "禁用"),
    ;

    ShopStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<String> getShopStatusListNoOk() {
        return ListUtil.toList(WAIT.code,FAIL.code,LOCK.code);
    }

    public static ShopStatus getByCode(String code) {
        for (ShopStatus status : ShopStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    private String code;

    private String desc;
}
