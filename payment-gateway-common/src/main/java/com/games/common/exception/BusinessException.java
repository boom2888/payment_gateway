package com.games.common.exception;

import com.games.common.utils.MessageUtils;

/**
 * 自定义异常
 * 
 * @author lor
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private Integer code = 500;

    private String message;

    public BusinessException(String message)
    {
        this.message = message;
    }

    public static BusinessException wait(String message)
    {
       try{
           return new BusinessException(MessageUtils.message(message));
       }catch (Exception e){
           return new BusinessException(message);
       }
    }

    public static BusinessException api(String message)
    {
        try{
            return new BusinessException(MessageUtils.message(message));
        }catch (Exception e){
            return new BusinessException(message);
        }
    }

    public BusinessException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, Throwable e)
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
