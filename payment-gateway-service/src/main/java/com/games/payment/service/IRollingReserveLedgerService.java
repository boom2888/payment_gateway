package com.games.payment.service;

import com.games.payment.domain.RollingReserveLedger;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 滚动准备金分类账Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IRollingReserveLedgerService extends IService<RollingReserveLedger> {

    /**
     * 查询滚动准备金分类账列表
     *
     * @param rollingReserveLedger 滚动准备金分类账
     * @return 滚动准备金分类账集合
     */
    List<RollingReserveLedger> selectAllList(RollingReserveLedger rollingReserveLedger);

    /**
     * 查询列表
     */
    List<RollingReserveLedger> queryList(RollingReserveLedger rollingReserveLedger);

}
