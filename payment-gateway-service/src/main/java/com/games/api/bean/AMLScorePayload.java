package com.games.api.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("AML 风险评分条目")
public class AMLScorePayload {

    @ApiModelProperty("名称")
    private String name;

    @JSONField(name = "group_name")
    @ApiModelProperty("所属分组名称")
    private String groupName;

    @ApiModelProperty("影响等级")
    private Integer impact;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("ID")
    private Integer id;

    @JSONField(name = "display_priority")
    @ApiModelProperty("显示优先级")
    private Integer displayPriority;

    @ApiModelProperty("描述")
    private String description;
}
