package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.api.enums.ApiStatus;
import com.games.api.enums.ApiType;
import com.games.payment.domain.ApiLog;

import java.util.List;

/**
 * 接口日志Service接口
 *
 * @author ç½ä½³ç
 * @date 2024-06-03
 */
public interface IApiLogService extends IService<ApiLog> {
    void saveLog(ApiType type, ApiStatus status, Long orderId, String ssoOrderId, String cardNo, String param, String response, Long shopId, String ip);
    /**
     * 查询接口日志列表
     *
     * @param ApiLog 接口日志
     * @return 接口日志集合
     */
    List<ApiLog> selectAllList(ApiLog ApiLog);

    /**
     * 查询列表
     */
    List<ApiLog> queryList(ApiLog ApiLog);

}
