package com.games.framework.config;

import com.games.common.constant.HttpStatus;
import com.games.common.core.domain.AjaxResult;
import com.games.common.exception.BaseException;
import com.games.common.exception.CustomException;
import com.games.common.exception.DemoModeException;
import com.games.common.utils.LangUtils;
import com.games.common.utils.StringUtils;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.games.common.utils.MessageUtils;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 *
 * @author lor
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public AjaxResult baseException(BaseException e)
    {
        return AjaxResult.error(e.getI18Message());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public AjaxResult businessException(CustomException e)
    {
        if (StringUtils.isNull(e.getCode()))
        {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public AjaxResult handlerNoFoundException(Exception e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error(HttpStatus.NOT_FOUND, MessageUtils.message("path.not.exist"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAuthorizationException(AccessDeniedException e)
    {
        log.error(e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, MessageUtils.message("no.permission"));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public AjaxResult handleAccountExpiredException(AccountExpiredException e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public AjaxResult handleUsernameNotFoundException(UsernameNotFoundException e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        // 如果报错信息有中文，说明是手动抛出的异常，需要返回给前端；如果是英文错误，说明是系统抛出的异常，不能把具体的错误信息返回给前端，直接返回服务器错误
        if (e.getMessage() != null && LangUtils.containsChinese(e.getMessage())) {
            return AjaxResult.error(e.getMessage());
        } else {
            return AjaxResult.error(MessageUtils.message("server.error"));
        }
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult validatedBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxResult constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = MessageUtils.message("parameter.validation.failed");
        // 先尝试获取字段级别的错误信息
        if (e.getBindingResult().getFieldError() != null) {
            message = e.getBindingResult().getFieldError().getDefaultMessage();
        }
        // 如果没有字段级别的错误，尝试获取全局错误信息（类级别的验证错误）
        else if (!e.getBindingResult().getGlobalErrors().isEmpty()) {
            message = e.getBindingResult().getGlobalErrors().get(0).getDefaultMessage();
        }
        return AjaxResult.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult demoModeException(DemoModeException e)
    {
        return AjaxResult.error(MessageUtils.message("demo.mode.not.allowed"));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public AjaxResult handleDuplicateKeyException(DuplicateKeyException e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error(MessageUtils.message("data.duplicate"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public AjaxResult handleDataIntegrityViolationException(DataIntegrityViolationException e)
    {
        log.error(e.getMessage(), e);
        Throwable cause = e.getCause();
        if (cause instanceof MysqlDataTruncation) {
            return AjaxResult.error(MessageUtils.message("data.length.exceed"));
        }
        return AjaxResult.error(MessageUtils.message("data.integrity.violation"));
    }

    /**
     * RuntimeException异常处理
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getMessage();
        
        // 如果异常消息不为空且不包含英文错误信息，直接返回（说明是国际化消息）
        if (StringUtils.isNotEmpty(message) && !message.contains("Exception") && !message.contains("Error")) {
            return AjaxResult.error(message);
        }
        
        // 否则返回通用的服务器错误消息
        return AjaxResult.error(MessageUtils.message("server.error"));
    }
}
