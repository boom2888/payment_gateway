package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.NuvieDisputeDataMapper;
import com.games.payment.domain.NuvieDisputeData;
import com.games.payment.service.INuvieDisputeDataService;

import java.util.List;
import java.util.Map;

/**
 * Nuvei争议数据Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class NuvieDisputeDataServiceImpl extends ServiceImpl<NuvieDisputeDataMapper, NuvieDisputeData> implements INuvieDisputeDataService {

    /**
     * 查询Nuvei争议数据列表
     *
     * @param nuvieDisputeData Nuvei争议数据
     * @return Nuvei争议数据
     */
    @Override
    public List<NuvieDisputeData> selectAllList(NuvieDisputeData nuvieDisputeData)
    {
        return getBaseMapper().selectAllList(nuvieDisputeData);
    }


    @Override
    public List<NuvieDisputeData> queryList(NuvieDisputeData nuvieDisputeData) {
        LambdaQueryWrapper<NuvieDisputeData> lqw = Wrappers.lambdaQuery();
        if (nuvieDisputeData.getMerchantId() != null){
            lqw.eq(NuvieDisputeData::getMerchantId ,nuvieDisputeData.getMerchantId());
        }
        if (nuvieDisputeData.getMethodName() != null){
            lqw.like(NuvieDisputeData::getMethodName ,nuvieDisputeData.getMethodName());
        }
        if (nuvieDisputeData.getCreatedAt() != null){
            lqw.eq(NuvieDisputeData::getCreatedAt ,nuvieDisputeData.getCreatedAt());
        }
        if (nuvieDisputeData.getUpdatedAt() != null){
            lqw.eq(NuvieDisputeData::getUpdatedAt ,nuvieDisputeData.getUpdatedAt());
        }
        return this.list(lqw);
    }




}
