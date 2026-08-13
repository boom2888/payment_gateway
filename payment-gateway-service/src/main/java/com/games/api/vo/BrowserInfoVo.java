package com.games.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BrowserInfoVo {

    @ApiModelProperty(value = "Accept header", example = "application/json", notes = "e.g. text/html, application/json")
    private String acceptHeader;

    @ApiModelProperty(value = "Whether Java is enabled", example = "False")
    private String javaEnabled;

    @ApiModelProperty(value = "Whether JavaScript is enabled", example = "False")
    private String javaScriptEnabled;

    @ApiModelProperty(value = "Language", example = "en-US", notes= "Browser language, e.g. zh-CN, en-US")
    private String language;

    @ApiModelProperty(value = "Color depth", example = "50", notes = "Bits per pixel (e.g. 24 or 32), used for image quality optimization or device identification")
    private String colorDepth;

    @ApiModelProperty(value = "Screen height", example = "30")
    private String screenHeight;

    @ApiModelProperty(value = "Screen width", example = "20")
    private String screenWidth;

    @ApiModelProperty(value = "Time zone", example = "+08", notes = "User time zone, e.g. GMT+8")
    private String timeZone;

    @ApiModelProperty(value = "User-Agent string", example = "Mozilla/5.0 Chrome/138.0.0.0 Safari/537.3")
    private String userAgent;

}
