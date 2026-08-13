package com.games.common.exception.user;

import com.games.common.utils.MessageUtils;

/**
 * 用户密码不正确或不符合规范异常类
 * 
 * @author lor
 */
public class UserPasswordNotMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException()
    {
        super(MessageUtils.message("user.password.not.match"), null);
    }
}
