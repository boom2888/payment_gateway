package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinderrRefreshTokenRequest {

    private String refresh_token;
    private String grant_type;
}
