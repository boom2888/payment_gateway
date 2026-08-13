package com.games.common.enums;

/**
 * Thunes 业务关系枚举
 * 
 * @author System
 * @date 2025-01-12
 */
public enum ThunesBusinessRelationship {
    
    /** 员工 */
    EMPLOYEE("EMPLOYEE", "员工"),
    
    /** 客户 */
    CUSTOMER("CUSTOMER", "客户"),
    
    /** 供应商服务 */
    VENDOR_SERVICE("VENDOR_SERVICE", "供应商服务"),
    
    /** 提供商 */
    PROVIDER("PROVIDER", "提供商"),
    
    /** 商业伙伴 */
    BUSINESS_PARTNER("BUSINESS_PARTNER", "商业伙伴"),
    
    /** 投资者 */
    INVESTOR("INVESTOR", "投资者"),
    
    /** 第三方 */
    THIRD_PARTY("THIRD_PARTY", "第三方"),
    
    /** 关联实体 */
    AFFILIATED_ENTITY("AFFILIATED_ENTITY", "关联实体"),
    
    /** 关联企业 */
    AFFILIATED_BUSINESS("AFFILIATED_BUSINESS", "关联企业");

    private final String code;
    private final String description;

    ThunesBusinessRelationship(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     */
    public static ThunesBusinessRelationship fromCode(String code) {
        for (ThunesBusinessRelationship relationship : values()) {
            if (relationship.getCode().equals(code)) {
                return relationship;
            }
        }
        return null;
    }

    /**
     * 验证代码是否有效
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }

    /**
     * 获取所有有效的代码字符串，用于验证注解
     */
    public static String getValidCodesPattern() {
        StringBuilder pattern = new StringBuilder("^(");
        for (int i = 0; i < values().length; i++) {
            if (i > 0) {
                pattern.append("|");
            }
            pattern.append(values()[i].getCode());
        }
        pattern.append(")?$");
        return pattern.toString();
    }
}