package com.games.logic;

/**
 * 业务操作类型
 * 
 * @author lor
 */
public enum AuthState
{
    AUTH_ING("审核中"),
    AUTH_FAIL("AUTH_FAIL"),
    AUTH_OK("AUTH_OK");

    public String code() {
        return desc;
    }

    private String desc;
    AuthState(String desc){
        this.desc = desc;
    }

}
