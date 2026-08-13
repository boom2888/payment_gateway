package com.games.payment.service;

import com.games.payment.domain.AuditLog;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 审计日志Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IAuditLogService extends IService<AuditLog> {

    /**
     * 查询审计日志列表
     *
     * @param auditLog 审计日志
     * @return 审计日志集合
     */
    List<AuditLog> selectAllList(AuditLog auditLog);

    /**
     * 查询列表
     */
    List<AuditLog> queryList(AuditLog auditLog);

}
