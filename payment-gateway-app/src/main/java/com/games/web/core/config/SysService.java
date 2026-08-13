package com.games.web.core.config;

import com.games.common.core.ISysService;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.entity.SysUser;
import org.springframework.stereotype.Service;

@Service
public class SysService implements ISysService {
    @Override
    public Object registUser(SysUser user) {
        return null;
    }

    @Override
    public AjaxResult checkRegisterUser(SysUser user) {
        return null;
    }
}
