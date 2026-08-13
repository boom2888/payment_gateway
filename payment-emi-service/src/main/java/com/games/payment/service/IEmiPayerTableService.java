package com.games.payment.service;

import com.games.payment.domain.EmiPayerTable;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 付款方列Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiPayerTableService extends IService<EmiPayerTable> {

    /**
     * 查询付款方列列表
     *
     * @param emiPayerTable 付款方列
     * @return 付款方列集合
     */
    List<EmiPayerTable> selectAllList(EmiPayerTable emiPayerTable);

    /**
     * 查询列表
     */
    List<EmiPayerTable> queryList(EmiPayerTable emiPayerTable);

}
