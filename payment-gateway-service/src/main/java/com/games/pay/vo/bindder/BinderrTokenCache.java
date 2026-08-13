package com.games.pay.vo.bindder;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class BinderrTokenCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    /** token 过期时间戳（毫秒） */
    private long expireAt;

    public boolean isExpired(long bufferMillis) {
        return System.currentTimeMillis() + bufferMillis >= expireAt;
    }
}
