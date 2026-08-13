package com.games.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Device Details")
public class DeviceDetailsVo {

    @ApiModelProperty(value = "Device IP address", required = true, example = "192.168.0.1")
    private String ipAddress;

    @ApiModelProperty(value = "3DS challenge window size", example = "04", notes = "01=250x400, 02=390x400, 03=500x600, 04=600x400, 05=full screen")
    private String challengeWindowSize;

    @ApiModelProperty(value = "3DS redirect URL", example = "https://example.com/3ds/callback")
    private String tdsRedirectUrl;

}
