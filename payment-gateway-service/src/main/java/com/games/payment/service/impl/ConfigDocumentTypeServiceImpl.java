package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigDocumentTypeMapper;
import com.games.payment.domain.ConfigDocumentType;
import com.games.payment.service.IConfigDocumentTypeService;

import java.util.List;
import java.util.Map;

/**
 * 证件类型配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigDocumentTypeServiceImpl extends ServiceImpl<ConfigDocumentTypeMapper, ConfigDocumentType> implements IConfigDocumentTypeService {

    /**
     * 查询证件类型配置列表
     *
     * @param configDocumentType 证件类型配置
     * @return 证件类型配置
     */
    @Override
    public List<ConfigDocumentType> selectAllList(ConfigDocumentType configDocumentType)
    {
        return getBaseMapper().selectAllList(configDocumentType);
    }


    @Override
    public List<ConfigDocumentType> queryList(ConfigDocumentType configDocumentType) {
        LambdaQueryWrapper<ConfigDocumentType> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configDocumentType.getCode())){
            lqw.eq(ConfigDocumentType::getCode ,configDocumentType.getCode());
        }
        if (configDocumentType.getStatus() != null){
            lqw.eq(ConfigDocumentType::getStatus ,configDocumentType.getStatus());
        }
        return this.list(lqw);
    }




}
