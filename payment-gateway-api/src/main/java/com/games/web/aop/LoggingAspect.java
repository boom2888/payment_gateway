package com.games.web.aop;

import com.games.api.enums.ApiStatus;
import com.games.api.enums.ApiType;
import com.games.common.annotation.LogApi;
import com.games.common.core.domain.R;
import com.games.common.utils.CardNumberUtils;
import com.games.payment.service.IApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private IApiLogService apiLogService;

    // 定义可能的订单ID字段名
    private static final List<String> ORDER_ID_FIELDS = Arrays.asList(
        "orderId", "orderNo", "id", "transferId", "transactionId", "externalId"
    );

    // 定义可能的商户ID字段名
    private static final List<String> MERCHANT_ID_FIELDS = Arrays.asList(
        "merchantId", "shopId", "companyId", "clientId"
    );

    // 定义可能的外部订单号字段名
    private static final List<String> EXTERNAL_ORDER_FIELDS = Arrays.asList(
        "ssoOrderId", "externalId", "refundSsoId", "outTradeNo", "merchantOrderId"
    );

    @Around("@annotation(com.games.common.annotation.LogApi)")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogApi logApi = method.getAnnotation(LogApi.class);

        RequestInfo requestInfo = new RequestInfo();
        try {
            Object[] args = pjp.getArgs();
            if (args.length > 0) {
                Object req = args[0];
                extractFieldsFromObject(req, requestInfo);
            }
            
            // 通过注解值或方法名确定API类型
            requestInfo.apiType = determineApiType(methodName, logApi.value());
        } catch (Exception e) {
            log.warn("提取请求信息失败: {}", e.getMessage());
        }

        try {
            Object result = pjp.proceed();
            // 从响应结果中提取orderId（如果请求中没有）
            if (requestInfo.orderId == null) {
                extractOrderIdFromResponse(result, requestInfo);
            }

            apiLogService.saveLog(requestInfo.apiType, ApiStatus.SUCC, requestInfo.orderId, requestInfo.ssoOrderId,
                    requestInfo.cardNo, null, null, requestInfo.shopId, requestInfo.ip);
            return result;
        } catch (Throwable ex) {
            apiLogService.saveLog(requestInfo.apiType, ApiStatus.FAIL, requestInfo.orderId, requestInfo.ssoOrderId,
                    requestInfo.cardNo, null, null, requestInfo.shopId, requestInfo.ip);
            throw ex;
        }
    }

    /**
     * 通用字段提取方法 - 支持多种可能的字段名
     */
    private void extractFieldsFromObject(Object obj, RequestInfo requestInfo) {
        if (obj == null) return;
        
        try {
            Class<?> clazz = obj.getClass();
            
            // 提取商户ID
            String merchantId = extractFieldByNames(obj, clazz, MERCHANT_ID_FIELDS, String.class);
            if (merchantId != null) {
                try {
                    requestInfo.shopId = Long.valueOf(merchantId);
                } catch (NumberFormatException ignore) {
                    log.debug("商户ID无法转换为Long: {}", merchantId);
                }
            }
            
            // 提取外部订单号
            requestInfo.ssoOrderId = extractFieldByNames(obj, clazz, EXTERNAL_ORDER_FIELDS, String.class);
            
            // 提取订单ID
            String orderIdStr = extractFieldByNames(obj, clazz, ORDER_ID_FIELDS, String.class);
            if (orderIdStr != null) {
                try {
                    requestInfo.orderId = Long.valueOf(orderIdStr);
                } catch (NumberFormatException e) {
                    log.debug("订单ID无法转换为Long: {}", orderIdStr);
                }
            }
            
            // 如果订单ID为空，尝试提取Long类型的订单ID
            if (requestInfo.orderId == null) {
                requestInfo.orderId = extractFieldByNames(obj, clazz, ORDER_ID_FIELDS, Long.class);
            }
            
            // 提取卡号信息
            requestInfo.cardNo = extractCardNumber(obj, clazz);
            
            // 提取IP地址
            requestInfo.ip = extractIpAddress(obj, clazz);
            
        } catch (Exception e) {
            log.warn("提取字段信息时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 通过多个可能的字段名提取值
     */
    private <T> T extractFieldByNames(Object obj, Class<?> clazz, List<String> fieldNames, Class<T> fieldType) {
        for (String fieldName : fieldNames) {
            T value = getFieldValue(obj, clazz, fieldName, fieldType);
            if (value != null) {
                log.debug("成功提取字段 {} = {}", fieldName, value);
                return value;
            }
        }
        return null;
    }

    private <T> T getFieldValue(Object obj, Class<?> clazz, String fieldName, Class<T> fieldType) {
        try {
            // 尝试通过getter方法获取
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = clazz.getMethod(getterName);
            Object value = getter.invoke(obj);
            if (value != null && fieldType.isAssignableFrom(value.getClass())) {
                return fieldType.cast(value);
            }
        } catch (Exception e) {
            // 如果getter方法失败，尝试直接访问字段
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null && fieldType.isAssignableFrom(value.getClass())) {
                    return fieldType.cast(value);
                }
            } catch (Exception ex) {
                // 忽略异常，继续尝试其他字段名
            }
        }
        return null;
    }

    /**
     * 提取卡号信息 - 支持多种可能的结构
     */
    private String extractCardNumber(Object obj, Class<?> clazz) {
        try {
            // 尝试多种可能的卡号字段结构
            String[] cardNumberPaths = {
                "cardInfo.cardNumber",
                "cardNumber", 
                "card.number",
                "paymentInfo.cardNumber"
            };
            
            for (String path : cardNumberPaths) {
                String cardNumber = extractNestedField(obj, path, String.class);
                if (cardNumber != null) {
                    // 对卡号进行脱敏处理
                    return CardNumberUtils.maskCardNumber(cardNumber);
                }
            }
        } catch (Exception e) {
            log.debug("无法提取卡号: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 提取IP地址 - 支持多种可能的结构
     */
    private String extractIpAddress(Object obj, Class<?> clazz) {
        try {
            String[] ipPaths = {
                "deviceDetail.ipAddress",
                "ipAddress",
                "clientIp",
                "remoteAddr"
            };
            
            for (String path : ipPaths) {
                String ip = extractNestedField(obj, path, String.class);
                if (ip != null) {
                    return ip;
                }
            }
        } catch (Exception e) {
            log.debug("无法提取IP地址: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 提取嵌套字段值
     */
    private <T> T extractNestedField(Object obj, String fieldPath, Class<T> fieldType) {
        if (obj == null || fieldPath == null) return null;
        
        String[] parts = fieldPath.split("\\.");
        Object current = obj;
        
        for (String part : parts) {
            if (current == null) return null;
            current = getFieldValue(current, current.getClass(), part, Object.class);
        }
        
        if (current != null && fieldType.isAssignableFrom(current.getClass())) {
            return fieldType.cast(current);
        }
        return null;
    }

    /**
     * 从响应中提取订单ID
     */
    private void extractOrderIdFromResponse(Object result, RequestInfo requestInfo) {
        try {
            if (result == null) return;

            // 从R对象中获取data
            Object data = getFieldValue(result, result.getClass(), "data", Object.class);
            if (data == null) return;

            // 尝试多种可能的订单ID字段
            for (String fieldName : ORDER_ID_FIELDS) {
                String orderNo = getFieldValue(data, data.getClass(), fieldName, String.class);
                if (orderNo != null) {
                    try {
                        requestInfo.orderId = Long.valueOf(orderNo);
                        log.debug("从响应中提取到订单ID: {} = {}", fieldName, requestInfo.orderId);
                        return;
                    } catch (NumberFormatException e) {
                        log.debug("响应中的{}无法转换为Long: {}", fieldName, orderNo);
                    }
                }
                
                // 尝试直接获取Long类型
                Long orderIdLong = getFieldValue(data, data.getClass(), fieldName, Long.class);
                if (orderIdLong != null) {
                    requestInfo.orderId = orderIdLong;
                    log.debug("从响应中提取到订单ID: {} = {}", fieldName, requestInfo.orderId);
                    return;
                }
            }
        } catch (Exception e) {
            log.debug("从响应中提取订单ID失败: {}", e.getMessage());
        }
    }

    /**
     * 通用API类型确定方法
     */
    private ApiType determineApiType(String methodName, String annotationValue) {
        // 优先使用注解中的值
        if (annotationValue != null && !annotationValue.trim().isEmpty()) {
            try {
                // 尝试将注解值转换为ApiType
                return ApiType.valueOf(annotationValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.debug("注解值无法转换为ApiType: {}", annotationValue);
            }
        }
        
        // 根据方法名推断API类型
        String lowerMethodName = methodName.toLowerCase();
        
        if (lowerMethodName.contains("create") && lowerMethodName.contains("order")) {
            return ApiType.CREATE_ORDER;
        } else if (lowerMethodName.contains("pay") && lowerMethodName.contains("order")) {
            return ApiType.PAY_ORDER;
        } else if (lowerMethodName.contains("refund")) {
            return ApiType.REFUND_ORDER;
        } else if (lowerMethodName.contains("swap")) {
            return ApiType.CREATE_SWAP_ORDER;
        } else if (lowerMethodName.contains("transfer")) {
            return ApiType.TRANSFER;
        } else if (lowerMethodName.contains("payment")) {
            return ApiType.PAYMENT;
        } else {
            log.debug("使用默认API类型，方法名: {}", methodName);
            return ApiType.OTHER;
        }
    }

    private static class RequestInfo {
        String paramJson = "";
        Long orderId = null;
        String ssoOrderId = null;
        String cardNo = "";
        String ip = "";
        Long shopId = null;
        ApiType apiType;
    }
}
