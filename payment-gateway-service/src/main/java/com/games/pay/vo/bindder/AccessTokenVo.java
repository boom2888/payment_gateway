package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenVo {

    private String client_id;
    private String client_secret;
    private String code;

}
