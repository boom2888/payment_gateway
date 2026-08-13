package com.games.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@ApiModel("3DS认证回掉")
@AllArgsConstructor
public class ThreedsCallbackDto extends BaseDto{
    private Boolean error;
    private String message;

}
