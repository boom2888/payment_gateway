package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.FeeTypeMapper;
import com.games.payment.domain.FeeType;
import com.games.payment.service.IFeeTypeService;

import java.util.List;
import java.util.Map;

/**
 * 费用类型Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class FeeTypeServiceImpl extends ServiceImpl<FeeTypeMapper, FeeType> implements IFeeTypeService {

    /**
     * 查询费用类型列表
     *
     * @param feeType 费用类型
     * @return 费用类型
     */
    @Override
    public List<FeeType> selectAllList(FeeType feeType)
    {
        return getBaseMapper().selectAllList(feeType);
    }


    @Override
    public List<FeeType> queryList(FeeType feeType) {
        LambdaQueryWrapper<FeeType> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(feeType.getName())){
            lqw.like(FeeType::getName ,feeType.getName());
        }
        if (feeType.getType() != null){
            lqw.eq(FeeType::getType ,feeType.getType());
        }
        if (feeType.getCreatedAt() != null){
            lqw.eq(FeeType::getCreatedAt ,feeType.getCreatedAt());
        }
        if (feeType.getCreatedBy() != null){
            lqw.eq(FeeType::getCreatedBy ,feeType.getCreatedBy());
        }
        if (feeType.getDeletedAt() != null){
            lqw.eq(FeeType::getDeletedAt ,feeType.getDeletedAt());
        }
        if (feeType.getDeletedBy() != null){
            lqw.eq(FeeType::getDeletedBy ,feeType.getDeletedBy());
        }
        if (feeType.getDeleted() != null){
            lqw.eq(FeeType::getDeleted ,feeType.getDeleted());
        }
        if (feeType.getRiskLevel() != null){
            lqw.eq(FeeType::getRiskLevel ,feeType.getRiskLevel());
        }
        if (feeType.getTransactionType() != null){
            lqw.eq(FeeType::getTransactionType ,feeType.getTransactionType());
        }
        return this.list(lqw);
    }




}
