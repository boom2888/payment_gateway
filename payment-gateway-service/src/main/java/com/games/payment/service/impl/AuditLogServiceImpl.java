package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.AuditLogMapper;
import com.games.payment.domain.AuditLog;
import com.games.payment.service.IAuditLogService;

import java.util.List;
import java.util.Map;

/**
 * 审计日志Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements IAuditLogService {

    /**
     * 查询审计日志列表
     *
     * @param auditLog 审计日志
     * @return 审计日志
     */
    @Override
    public List<AuditLog> selectAllList(AuditLog auditLog)
    {
        return getBaseMapper().selectAllList(auditLog);
    }


    @Override
    public List<AuditLog> queryList(AuditLog auditLog) {
        LambdaQueryWrapper<AuditLog> lqw = Wrappers.lambdaQuery();
        if (auditLog.getAuditEntityType() != null){
            lqw.eq(AuditLog::getAuditEntityType ,auditLog.getAuditEntityType());
        }
        if (auditLog.getEntityId() != null){
            lqw.eq(AuditLog::getEntityId ,auditLog.getEntityId());
        }
        if (auditLog.getSaasUserCorporationId() != null){
            lqw.eq(AuditLog::getSaasUserCorporationId ,auditLog.getSaasUserCorporationId());
        }
        if (auditLog.getRequesterId() != null){
            lqw.eq(AuditLog::getRequesterId ,auditLog.getRequesterId());
        }
        if (StringUtils.isNotBlank(auditLog.getContent())){
            lqw.eq(AuditLog::getContent ,auditLog.getContent());
        }
        if (auditLog.getCreatedAt() != null){
            lqw.eq(AuditLog::getCreatedAt ,auditLog.getCreatedAt());
        }
        if (auditLog.getCreatedBy() != null){
            lqw.eq(AuditLog::getCreatedBy ,auditLog.getCreatedBy());
        }
        if (auditLog.getDeletedAt() != null){
            lqw.eq(AuditLog::getDeletedAt ,auditLog.getDeletedAt());
        }
        if (auditLog.getDeletedBy() != null){
            lqw.eq(AuditLog::getDeletedBy ,auditLog.getDeletedBy());
        }
        if (auditLog.getDeleted() != null){
            lqw.eq(AuditLog::getDeleted ,auditLog.getDeleted());
        }
        if (auditLog.getAction() != null){
            lqw.eq(AuditLog::getAction ,auditLog.getAction());
        }
        return this.list(lqw);
    }




}
