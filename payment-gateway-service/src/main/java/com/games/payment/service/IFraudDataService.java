package com.games.payment.service;

import com.games.payment.domain.FraudData;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 欺诈数据Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IFraudDataService extends IService<FraudData> {

    /**
     * 查询欺诈数据列表
     *
     * @param fraudData 欺诈数据
     * @return 欺诈数据集合
     */
    List<FraudData> selectAllList(FraudData fraudData);

    /**
     * 查询列表
     */
    List<FraudData> queryList(FraudData fraudData);

}
