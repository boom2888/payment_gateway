package com.games.payment.service;

import com.games.payment.domain.NuvieDisputeData;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * Nuvei争议数据Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface INuvieDisputeDataService extends IService<NuvieDisputeData> {

    /**
     * 查询Nuvei争议数据列表
     *
     * @param nuvieDisputeData Nuvei争议数据
     * @return Nuvei争议数据集合
     */
    List<NuvieDisputeData> selectAllList(NuvieDisputeData nuvieDisputeData);

    /**
     * 查询列表
     */
    List<NuvieDisputeData> queryList(NuvieDisputeData nuvieDisputeData);

}
