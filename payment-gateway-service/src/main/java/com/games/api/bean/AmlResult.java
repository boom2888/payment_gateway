package com.games.api.bean;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel("AML 风险评分结果")
public class AmlResult {

    @ApiModelProperty("评分记录ID")
    private String id;

    @ApiModelProperty("风险条目列表")
    private List<AMLScorePayload> payload;

    @JsonProperty("risk_level")
    @ApiModelProperty("风险等级")
    private String riskLevel;

    @ApiModelProperty("风险评分")
    private Integer score;
}
