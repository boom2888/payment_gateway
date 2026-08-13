package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.MerchantFeeModelMapper;
import com.games.payment.domain.MerchantFeeModel;
import com.games.payment.service.IMerchantFeeModelService;

import java.util.List;
import java.util.Map;

/**
 * 商户收费模型Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-18
 */
@Service
public class MerchantFeeModelServiceImpl extends ServiceImpl<MerchantFeeModelMapper, MerchantFeeModel> implements IMerchantFeeModelService {

    /**
     * 查询商户收费模型列表
     *
     * @param merchantFeeModel 商户收费模型
     * @return 商户收费模型
     */
    @Override
    public List<MerchantFeeModel> selectAllList(MerchantFeeModel merchantFeeModel)
    {
        return getBaseMapper().selectAllList(merchantFeeModel);
    }


    @Override
    public List<MerchantFeeModel> queryList(MerchantFeeModel merchantFeeModel) {
        LambdaQueryWrapper<MerchantFeeModel> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(merchantFeeModel.getName())){
            lqw.like(MerchantFeeModel::getName ,merchantFeeModel.getName());
        }
        return this.list(lqw);
    }




}
