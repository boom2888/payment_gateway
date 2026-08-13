package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.CustomTokenMapping;

import java.util.List;

public interface ICustomerTokenMappingService extends IService<CustomTokenMapping> {

    List<CustomTokenMapping> getByCardNumber(String cardNumber);

    List<CustomTokenMapping> getByCardNumber(String cardNumber, int limit);

    List<CustomTokenMapping> getByCardNumber(String cardNumber, String lastUsedOrderId);

    List<CustomTokenMapping> getByCardNumber(String cardNumber, Long customerId);

    /**
     * 查询所有记录
     * @param customTokenMapping CustomTokenMapping
     * @return List<CustomTokenMapping>
     */
    List<CustomTokenMapping> selectAllList(CustomTokenMapping customTokenMapping);

    /**
     * 查询列表
     */
    List<CustomTokenMapping> queryList(CustomTokenMapping customTokenMapping);

    /**
     * 计数自增，命中阈值时归零
     *
     * @param id       记录ID
     * @param maxCount 触发归零的阈值（含该值）
     * @return true 表示更新成功
     */
    boolean increaseCountAndReset(Long id, int maxCount);

    /**
     * 当 customerId 为空时，用传入的 customerId 进行填充
     *
     * @param userTokenId      用户 Token
     * @param paymentOptionId  支付选项 ID
     * @param cardNumber       卡号，可选
     * @param customerId       待写入的客户 ID
     * @return true 表示至少有一条记录被更新
     */
    boolean fillCustomerIdIfAbsent(String userTokenId, String paymentOptionId, String cardNumber, Long customerId);
}
