package com.games.payment.mapper;

import com.games.payment.domain.NuvieDisputeData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * Nuvei争议数据Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface NuvieDisputeDataMapper extends BaseMapper<NuvieDisputeData> {
    /**
     * 查询Nuvei争议数据列表
     *
     * @param nuvieDisputeData Nuvei争议数据
     * @return Nuvei争议数据集合
     */
    List<NuvieDisputeData> selectAllList(NuvieDisputeData nuvieDisputeData);

}
