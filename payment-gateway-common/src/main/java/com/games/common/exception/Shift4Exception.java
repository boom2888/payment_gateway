package com.games.common.exception;

import com.games.common.utils.MessageUtils;
import lombok.Getter;

/**
 * 自定义异常
 *
 * @author lor
 */
public class Shift4Exception extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    @Getter
    private Integer code = 500;

    private final String message;

    public Shift4Exception(String message)
    {
        this.message = message;
    }

    public static Shift4Exception wait(String message)
    {
        try
        {
            return new Shift4Exception(MessageUtils.message(message));
        }
        catch (Exception e)
        {
            return new Shift4Exception(message);
        }
    }

    public static Shift4Exception api(String message)
    {
        try
        {
            return new Shift4Exception(MessageUtils.message(message));
        }
        catch (Exception e)
        {
            return new Shift4Exception(message);
        }
    }

    public Shift4Exception(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public Shift4Exception(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

}
