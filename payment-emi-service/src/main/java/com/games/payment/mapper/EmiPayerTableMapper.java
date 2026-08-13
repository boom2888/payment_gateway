package com.games.payment.mapper;

import com.games.payment.domain.EmiPayerTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 付款方列Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiPayerTableMapper extends BaseMapper<EmiPayerTable> {
    /**
     * 查询付款方列列表
     *
     * @param emiPayerTable 付款方列
     * @return 付款方列集合
     */
    List<EmiPayerTable> selectAllList(EmiPayerTable emiPayerTable);

}
