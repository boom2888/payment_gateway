package com.games.payment.service;

import com.games.payment.domain.EmiPayee;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 收款方列Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiPayeeService extends IService<EmiPayee> {

    /**
     * 查询收款方列列表
     *
     * @param emiPayee 收款方列
     * @return 收款方列集合
     */
    List<EmiPayee> selectAllList(EmiPayee emiPayee);

    /**
     * 查询列表
     */
    List<EmiPayee> queryList(EmiPayee emiPayee);

}
