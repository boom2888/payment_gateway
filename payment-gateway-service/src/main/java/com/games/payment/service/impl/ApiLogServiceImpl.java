package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.api.enums.ApiStatus;
import com.games.api.enums.ApiType;
import com.games.payment.domain.ApiLog;
import com.games.payment.mapper.ApiLogMapper;
import com.games.payment.service.IApiLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 接口日志Service业务层处理
 *
 * @author ç½ä½³ç
 * @date 2024-06-03
 */
@Service
public class ApiLogServiceImpl extends ServiceImpl<ApiLogMapper, ApiLog> implements IApiLogService {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) //新事物去存
    public void saveLog(ApiType type, ApiStatus status, Long orderId, String ssoOrderId, String cardNo, String param, String response, Long shopId, String ip) {
        ApiLog log =  ApiLog.init(type, status, orderId, ssoOrderId, cardNo, param, response, shopId, ip);
        super.save(log);
    }

    /**
     * 查询接口日志列表
     *
     * @param ApiLog 接口日志
     * @return 接口日志
     */
    @Override
    public List<ApiLog> selectAllList(ApiLog ApiLog)
    {
        return getBaseMapper().selectAllList(ApiLog);
    }


    @Override
    public List<ApiLog> queryList(ApiLog ApiLog) {
        LambdaQueryWrapper<ApiLog> lqw = Wrappers.lambdaQuery();
//        if (StringUtils.isNotBlank(ApiLog.getMobile())){
//            lqw.eq(ApiLog::getMobile ,ApiLog.getMobile());
//        }
//        if (StringUtils.isNotBlank(ApiLog.getParams())){
//            lqw.like(ApiLog::getParams ,ApiLog.getParams());
//        }
//        if (StringUtils.isNotBlank(ApiLog.getStatus())){
//            lqw.eq(ApiLog::getStatus ,ApiLog.getStatus());
//        }
//        if (ApiLog.getCreateTime() != null){
//            lqw.eq(ApiLog::getCreateTime ,ApiLog.getCreateTime());
//        }
        return this.list(lqw);
    }




}
