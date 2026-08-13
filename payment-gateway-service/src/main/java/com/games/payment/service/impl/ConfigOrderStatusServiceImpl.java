package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigOrderStatusMapper;
import com.games.payment.domain.ConfigOrderStatus;
import com.games.payment.service.IConfigOrderStatusService;

import java.util.List;
import java.util.Map;

/**
 * 订单状态配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigOrderStatusServiceImpl extends ServiceImpl<ConfigOrderStatusMapper, ConfigOrderStatus> implements IConfigOrderStatusService {

    /**
     * 查询订单状态配置列表
     *
     * @param configOrderStatus 订单状态配置
     * @return 订单状态配置
     */
    @Override
    public List<ConfigOrderStatus> selectAllList(ConfigOrderStatus configOrderStatus)
    {
        return getBaseMapper().selectAllList(configOrderStatus);
    }


    @Override
    public List<ConfigOrderStatus> queryList(ConfigOrderStatus configOrderStatus) {
        LambdaQueryWrapper<ConfigOrderStatus> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configOrderStatus.getCode())){
            lqw.eq(ConfigOrderStatus::getCode ,configOrderStatus.getCode());
        }
        if (StringUtils.isNotBlank(configOrderStatus.getDesc())){
            lqw.eq(ConfigOrderStatus::getDesc ,configOrderStatus.getDesc());
        }
        if (configOrderStatus.getCreatedAt() != null){
            lqw.eq(ConfigOrderStatus::getCreatedAt ,configOrderStatus.getCreatedAt());
        }
        if (configOrderStatus.getCreatedBy() != null){
            lqw.eq(ConfigOrderStatus::getCreatedBy ,configOrderStatus.getCreatedBy());
        }
        if (configOrderStatus.getDeletedAt() != null){
            lqw.eq(ConfigOrderStatus::getDeletedAt ,configOrderStatus.getDeletedAt());
        }
        if (configOrderStatus.getDeletedBy() != null){
            lqw.eq(ConfigOrderStatus::getDeletedBy ,configOrderStatus.getDeletedBy());
        }
        if (configOrderStatus.getDeleted() != null){
            lqw.eq(ConfigOrderStatus::getDeleted ,configOrderStatus.getDeleted());
        }
        return this.list(lqw);
    }




}
