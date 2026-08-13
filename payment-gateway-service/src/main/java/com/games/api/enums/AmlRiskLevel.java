package com.games.api.enums;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "AML 风险等级枚举")
public enum AmlRiskLevel {

    @ApiModelProperty("低风险")
    LOW("low", "低风险"),

    @ApiModelProperty("中等风险")
    MEDIUM("medium", "中等风险"),

    @ApiModelProperty("高风险")
    HIGH("high", "高风险"),

    @ApiModelProperty("严重风险")
    SEVERE("severe", "严重风险"),

    @ApiModelProperty("未知风险")
    UNKNOWN("unknown", "未知风险");

    private final String level;
    private final String description;

    AmlRiskLevel(String level, String description) {
        this.level = level;
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public static AmlRiskLevel fromLevel(String level) {
        for (AmlRiskLevel riskLevel : AmlRiskLevel.values()) {
            if (riskLevel.level.equalsIgnoreCase(level)) {
                return riskLevel;
            }
        }
        return UNKNOWN;
    }
}
