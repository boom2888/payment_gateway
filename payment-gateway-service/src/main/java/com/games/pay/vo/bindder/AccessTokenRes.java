package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenRes {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
}
