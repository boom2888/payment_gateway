package com.games.payment.mapper;

import com.games.payment.domain.FeeType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 费用类型Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface FeeTypeMapper extends BaseMapper<FeeType> {
    /**
     * 查询费用类型列表
     *
     * @param feeType 费用类型
     * @return 费用类型集合
     */
    List<FeeType> selectAllList(FeeType feeType);

}
