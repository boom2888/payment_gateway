package com.games.logic;

/**
 * 业务操作类型
 * 
 * @author lor
 */
public enum BooleanType
{
    NO("N"),
    YES("Y"),
    NONE("W");

    public String code() {
        return desc;
    }

    private String desc;
    BooleanType(String desc){
        this.desc = desc;
    }

}
