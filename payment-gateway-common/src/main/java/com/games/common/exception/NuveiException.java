package com.games.common.exception;

import com.games.common.utils.MessageUtils;

/**
 * 自定义异常
 * 
 * @author lor
 */
public class NuveiException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private Integer code = 500;

    private String message;

    public NuveiException(String message)
    {
        this.message = message;
    }

    public static NuveiException wait(String message)
    {
       try{
           return new NuveiException(MessageUtils.message(message));
       }catch (Exception e){
           return new NuveiException(message);
       }
    }

    public static NuveiException api(String message)
    {
        try{
            return new NuveiException(MessageUtils.message(message));
        }catch (Exception e){
            return new NuveiException(message);
        }
    }

    public NuveiException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public NuveiException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }
}
