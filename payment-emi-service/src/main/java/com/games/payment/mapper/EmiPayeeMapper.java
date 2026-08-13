package com.games.payment.mapper;

import com.games.payment.domain.EmiPayee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 收款方列Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiPayeeMapper extends BaseMapper<EmiPayee> {
    /**
     * 查询收款方列列表
     *
     * @param emiPayee 收款方列
     * @return 收款方列集合
     */
    List<EmiPayee> selectAllList(EmiPayee emiPayee);

}
