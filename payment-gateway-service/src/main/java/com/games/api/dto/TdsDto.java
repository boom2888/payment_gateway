package com.games.api.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@ApiModel("3DS Authentication")
@NoArgsConstructor
@AllArgsConstructor
public class TdsDto extends BaseDto {
    // ========== 3DS Challenge流程字段 ==========
    @ApiModelProperty(value = "3DS authentication URL (Challenge flow)", example = "https://acs.example.com")
    private String acsUrl = null;

    @ApiModelProperty(value = "3DS authentication key (Challenge flow)")
    @JsonProperty("cReq")
    private String cReq;

    // ========== 3DS Method (DFP)流程字段 ==========
    @ApiModelProperty(value = "3DS Method URL (DFP flow) - frontend should POST to this URL in an iframe")
    private String tdsMethodUrl;

    @ApiModelProperty(value = "3DS Method Data (DFP flow) - Base64-encoded JSON data, sent as POST body")
    private String threeDSMethodData;

    @ApiModelProperty(value = "3DS transaction ID - used for completion call")
    private String tdsTrxId;

    @ApiModelProperty(value = "Original transaction z1 - used as g5 parameter in completion call")
    private String originalZ1;

    /**
     * Challenge流程构造函数
     */
    public TdsDto(String acsUrl, String cReq) {
        this.acsUrl = acsUrl;
        this.cReq = cReq;
    }

    /**
     * DFP流程构造函数
     */
    public static TdsDto forDfp(String tdsMethodUrl, String threeDSMethodData, String tdsTrxId, String originalZ1) {
        TdsDto dto = new TdsDto();
        dto.setTdsMethodUrl(tdsMethodUrl);
        dto.setThreeDSMethodData(threeDSMethodData);
        dto.setTdsTrxId(tdsTrxId);
        dto.setOriginalZ1(originalZ1);
        return dto;
    }
}
