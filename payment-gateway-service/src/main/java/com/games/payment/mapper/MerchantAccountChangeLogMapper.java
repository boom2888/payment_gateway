package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.MerchantAccountChangeLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户资产变更记录Mapper接口
 *
 * @author Ticker
 * @date 2025-11-27
 */
public interface MerchantAccountChangeLogMapper extends BaseMapper<MerchantAccountChangeLog> {

     /**
      * 批量插入变更记录
      *
      * @param changeLogs 变更记录列表
      * @return 插入的记录数
      */
    int batchInsert(@Param("changeLogs") List<MerchantAccountChangeLog> changeLogs);

    /**
     * 根据对账报告ID和变更类型查询变更记录
     *
     * @param movementImportId 对账报告ID
     * @param type             变更类型
     * @return 变更记录列表
     */
    List<MerchantAccountChangeLog> selectByMovementImportId(@Param("movementImportId") Long movementImportId,
                                                            @Param("type") Integer type);
}
