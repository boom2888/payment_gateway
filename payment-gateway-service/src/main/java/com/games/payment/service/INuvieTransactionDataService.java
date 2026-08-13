package com.games.payment.service;

import com.games.payment.domain.NuvieTransactionData;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * Nuvei交易数据Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface INuvieTransactionDataService extends IService<NuvieTransactionData> {

    /**
     * 查询Nuvei交易数据列表
     *
     * @param nuvieTransactionData Nuvei交易数据
     * @return Nuvei交易数据集合
     */
    List<NuvieTransactionData> selectAllList(NuvieTransactionData nuvieTransactionData);

    /**
     * 查询列表
     */
    List<NuvieTransactionData> queryList(NuvieTransactionData nuvieTransactionData);

}
