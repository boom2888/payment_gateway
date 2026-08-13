package com.games.payment.mapper;

import com.games.payment.domain.AuditLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 审计日志Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface AuditLogMapper extends BaseMapper<AuditLog> {
    /**
     * 查询审计日志列表
     *
     * @param auditLog 审计日志
     * @return 审计日志集合
     */
    List<AuditLog> selectAllList(AuditLog auditLog);

}
