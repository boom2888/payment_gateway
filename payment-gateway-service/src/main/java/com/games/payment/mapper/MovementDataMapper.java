package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.pay.dto.DailySummaryDto;
import com.games.pay.dto.NuvieDayDto;
import com.games.pay.dto.OrderStatusStatistics;
import com.games.pay.dto.ReconReportAllDto;
import com.games.pay.vo.OrderAmountStatistics;
import com.games.payment.domain.MovementData;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * nuvei导入数据明细Mapper接口
 *
 * @author Ticker
 * @date 2025-07-21
 */
public interface MovementDataMapper extends BaseMapper<MovementData> {
    /**
     * 查询nuvei导入数据明细列表
     *
     * @param movementData nuvei导入数据明细
     * @return nuvei导入数据明细集合
     */
    List<MovementData> selectAllList(MovementData movementData);

    List<ReconReportAllDto> export(Long movementImportId);

    List<MovementData> exportRawData(Long movementImportId);

    String selectMonth(String movementImportId);

    String selectMonths(@Param("movementImportIds") List<String> movementImportIds);

    List<String> selectDays(@Param("movementImportIds") List<String> movementImportId);

    List<NuvieDayDto> selectBatchDays(@Param("movementImportIds") List<String> movementImportId);

    List<DailySummaryDto> selectDailySummarys(@Param("movementImportIds") List<String> movementImportIds);

    String selectLastDay(String movementImportId);

    List<MovementData> selectRawData(@Param("movementImportIds") List<Long> movementImportIds);

    List<MovementData> merchantSettlement(Long movementImportId);

    /**
     * 根据日期范围和类型统计交易数量
     *
     * @param startDate     开始日期
     * @param endDate       结束日期
     * @param movementTypes 交易类型
     * @param shopId        商户ID
     * @return 按日期分组的交易数量统计
     */
    List<OrderStatusStatistics> countByDateAndType(@Param("startDate") String startDate,
                                                   @Param("endDate") String endDate,
                                                   @Param("movementTypes") List<String> movementTypes,
                                                   @Param("shopId") Long shopId);

    List<OrderAmountStatistics> getAmountByDateAndType(@Param("startDate") String startDate,
                                                       @Param("endDate") String endDate,
                                                       @Param("movementTypes") List<String> movementTypes,
                                                       @Param("shopId") Long shopId);

    /**
     * 根据日期范围和类型统计去重后的交易数量
     *
     * @param startDate    开始日期
     * @param endDate      结束日期
     * @param movementType 交易类型
     * @param shopId       商户ID
     * @return 去重后的交易数量
     */
    int getDistinctCountByDateAndType(@Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("movementType") String movementType,
                                      @Param("shopId") Long shopId);


    /**
     * 计算商户可提取余额
     * @param merchantId 商户号
     * @return 商户可提取余额
     **/
    BigDecimal calMerchantSettlementFeeTotal(Long merchantId);

    /**
     * 根据导入ID删除数据
     * @param movementImportId 导入ID
     * @return 删除的行数
     */
    int deleteByMovementImportId(Long movementImportId);
}
