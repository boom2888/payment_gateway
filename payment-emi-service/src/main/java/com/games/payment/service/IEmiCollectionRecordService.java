package com.games.payment.service;

import com.games.payment.domain.EmiCollectionRecord;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 收款记录Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiCollectionRecordService extends IService<EmiCollectionRecord> {

    /**
     * 查询收款记录列表
     *
     * @param emiCollectionRecord 收款记录
     * @return 收款记录集合
     */
    List<EmiCollectionRecord> selectAllList(EmiCollectionRecord emiCollectionRecord);

    /**
     * 查询列表
     */
    List<EmiCollectionRecord> queryList(EmiCollectionRecord emiCollectionRecord);

}
