package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantBalanceVo extends ApiBaseReq {

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return super.getMerchantId();
    }
}
