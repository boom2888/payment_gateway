package com.games.payment.mapper;

import com.games.payment.domain.NuvieTransactionData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * Nuvei交易数据Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface NuvieTransactionDataMapper extends BaseMapper<NuvieTransactionData> {
    /**
     * 查询Nuvei交易数据列表
     *
     * @param nuvieTransactionData Nuvei交易数据
     * @return Nuvei交易数据集合
     */
    List<NuvieTransactionData> selectAllList(NuvieTransactionData nuvieTransactionData);

}
