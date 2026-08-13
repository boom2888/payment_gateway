package com.games.common.core;

import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.entity.SysUser;

public interface ISysService {
    Object registUser(SysUser user);

    /**
     * 注册用户校验
     * @param user 用户信息
     */
    AjaxResult checkRegisterUser(SysUser user);
}
