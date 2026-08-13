package com.games.payment.mapper;

import com.games.payment.domain.MerchantFeeModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 商户收费模型Mapper接口
 *
 * @author Ticker
 * @date 2025-07-18
 */
public interface MerchantFeeModelMapper extends BaseMapper<MerchantFeeModel> {
    /**
     * 查询商户收费模型列表
     *
     * @param merchantFeeModel 商户收费模型
     * @return 商户收费模型集合
     */
    List<MerchantFeeModel> selectAllList(MerchantFeeModel merchantFeeModel);

}
