package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.ThunesPayer;
import com.games.thunes.dto.PayerDto;

import java.util.List;

/**
 * Thunes Payer 服务接口
 *
 * @author System
 * @date 2025-01-09
 */
public interface IThunesPayerService extends IService<ThunesPayer> {

    /**
     * 根据国家和货币查询 Payers
     */
    List<ThunesPayer> getPayersByCountryAndCurrency(String countryCode, String currency);

    /**
     * 根据国家查询 Payers
     */
    List<ThunesPayer> getPayersByCountry(String countryCode);

    /**
     * 根据货币查询 Payers
     */
    List<ThunesPayer> getPayersByCurrency(String currency);

    /**
     * 获取所有活跃的 Payers
     */
    List<ThunesPayer> getAllActivePayers();

    /**
     * 将 PayerDto 转换为 ThunesPayer
     */
    ThunesPayer convertFromDto(PayerDto payerDto);

    /**
     * 将 ThunesPayer 转换为 PayerDto
     */
    PayerDto convertToDto(ThunesPayer thunesPayer);

    /**
     * 批量保存或更新 Payers
     */
    void saveOrUpdateBatch(List<ThunesPayer> payers);
}