package com.games.payment.mapper;

import com.games.payment.domain.RollingReserveLedger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 滚动准备金分类账Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface RollingReserveLedgerMapper extends BaseMapper<RollingReserveLedger> {
    /**
     * 查询滚动准备金分类账列表
     *
     * @param rollingReserveLedger 滚动准备金分类账
     * @return 滚动准备金分类账集合
     */
    List<RollingReserveLedger> selectAllList(RollingReserveLedger rollingReserveLedger);

}
