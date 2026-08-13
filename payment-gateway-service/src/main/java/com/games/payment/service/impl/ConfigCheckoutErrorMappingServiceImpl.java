package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigCheckoutErrorMappingMapper;
import com.games.payment.domain.ConfigCheckoutErrorMapping;
import com.games.payment.service.IConfigCheckoutErrorMappingService;

import java.util.List;
import java.util.Map;

/**
 * Checkout错误映射配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigCheckoutErrorMappingServiceImpl extends ServiceImpl<ConfigCheckoutErrorMappingMapper, ConfigCheckoutErrorMapping> implements IConfigCheckoutErrorMappingService {

    /**
     * 查询Checkout错误映射配置列表
     *
     * @param configCheckoutErrorMapping Checkout错误映射配置
     * @return Checkout错误映射配置
     */
    @Override
    public List<ConfigCheckoutErrorMapping> selectAllList(ConfigCheckoutErrorMapping configCheckoutErrorMapping)
    {
        return getBaseMapper().selectAllList(configCheckoutErrorMapping);
    }


    @Override
    public List<ConfigCheckoutErrorMapping> queryList(ConfigCheckoutErrorMapping configCheckoutErrorMapping) {
        LambdaQueryWrapper<ConfigCheckoutErrorMapping> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configCheckoutErrorMapping.getCode())){
            lqw.eq(ConfigCheckoutErrorMapping::getCode ,configCheckoutErrorMapping.getCode());
        }
        if (StringUtils.isNotBlank(configCheckoutErrorMapping.getMessage())){
            lqw.eq(ConfigCheckoutErrorMapping::getMessage ,configCheckoutErrorMapping.getMessage());
        }
        return this.list(lqw);
    }




}
