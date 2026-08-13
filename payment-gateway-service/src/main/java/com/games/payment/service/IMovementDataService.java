package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.MovementData;

import java.math.BigDecimal;
import java.util.List;

/**
 * nuvei导入数据明细Service接口
 *
 * @author Ticker
 * @date 2025-07-21
 */
public interface IMovementDataService extends IService<MovementData> {

    /**
     * 查询nuvei导入数据明细列表
     *
     * @param movementData nuvei导入数据明细
     * @return nuvei导入数据明细集合
     */
    List<MovementData> selectAllList(MovementData movementData);

    /**
     * 查询列表
     */
    List<MovementData> queryList(MovementData movementData);

    void calculateMerchantSettlementFee(Long movementImportId);

    /**
     * 计算商户可提取余额
     * @param merchantId 商户号
     * @return 商户可提取余额
     */
    BigDecimal calMerchantSettlementFeeTotal(Long merchantId);

    void removeImportAndMovementData(List<String> movementImportIds);
}
