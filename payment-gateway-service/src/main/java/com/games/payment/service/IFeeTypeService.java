package com.games.payment.service;

import com.games.payment.domain.FeeType;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 费用类型Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IFeeTypeService extends IService<FeeType> {

    /**
     * 查询费用类型列表
     *
     * @param feeType 费用类型
     * @return 费用类型集合
     */
    List<FeeType> selectAllList(FeeType feeType);

    /**
     * 查询列表
     */
    List<FeeType> queryList(FeeType feeType);

}
