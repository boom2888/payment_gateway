package com.games.payment.mapper;

import com.games.payment.domain.FraudData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 欺诈数据Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface FraudDataMapper extends BaseMapper<FraudData> {
    /**
     * 查询欺诈数据列表
     *
     * @param fraudData 欺诈数据
     * @return 欺诈数据集合
     */
    List<FraudData> selectAllList(FraudData fraudData);

}
