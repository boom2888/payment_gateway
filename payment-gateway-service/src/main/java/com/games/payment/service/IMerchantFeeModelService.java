package com.games.payment.service;

import com.games.payment.domain.MerchantFeeModel;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 商户收费模型Service接口
 *
 * @author Ticker
 * @date 2025-07-18
 */
public interface IMerchantFeeModelService extends IService<MerchantFeeModel> {

    /**
     * 查询商户收费模型列表
     *
     * @param merchantFeeModel 商户收费模型
     * @return 商户收费模型集合
     */
    List<MerchantFeeModel> selectAllList(MerchantFeeModel merchantFeeModel);

    /**
     * 查询列表
     */
    List<MerchantFeeModel> queryList(MerchantFeeModel merchantFeeModel);

}
