package com.games.payment.mapper;

import com.games.payment.domain.EmiCollectionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 收款记录Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiCollectionRecordMapper extends BaseMapper<EmiCollectionRecord> {
    /**
     * 查询收款记录列表
     *
     * @param emiCollectionRecord 收款记录
     * @return 收款记录集合
     */
    List<EmiCollectionRecord> selectAllList(EmiCollectionRecord emiCollectionRecord);

}
