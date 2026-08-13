package com.games.payment.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.payment.domain.ThunesPayer;
import com.games.payment.mapper.ThunesPayerMapper;
import com.games.payment.service.IThunesPayerService;
import com.games.thunes.dto.PayerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thunes Payer 服务实现类
 *
 * @author System
 * @date 2025-01-09
 */
@Slf4j
@Service
public class ThunesPayerServiceImpl extends ServiceImpl<ThunesPayerMapper, ThunesPayer> implements IThunesPayerService {

    @Override
    public List<ThunesPayer> getPayersByCountryAndCurrency(String countryCode, String currency) {
        LambdaQueryWrapper<ThunesPayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThunesPayer::getCountryCode, countryCode)
               .eq(ThunesPayer::getCurrency, currency)
               .eq(ThunesPayer::getIsActive, true)
               .orderByAsc(ThunesPayer::getName);
        
        return list(wrapper);
    }

    @Override
    public List<ThunesPayer> getPayersByCountry(String countryCode) {
        LambdaQueryWrapper<ThunesPayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThunesPayer::getCountryCode, countryCode)
               .eq(ThunesPayer::getIsActive, true)
               .orderByAsc(ThunesPayer::getName);
        
        return list(wrapper);
    }

    @Override
    public List<ThunesPayer> getPayersByCurrency(String currency) {
        LambdaQueryWrapper<ThunesPayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThunesPayer::getCurrency, currency)
               .eq(ThunesPayer::getIsActive, true)
               .orderByAsc(ThunesPayer::getName);
        
        return list(wrapper);
    }

    @Override
    public List<ThunesPayer> getAllActivePayers() {
        LambdaQueryWrapper<ThunesPayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThunesPayer::getIsActive, true)
               .orderByAsc(ThunesPayer::getCountryCode)
               .orderByAsc(ThunesPayer::getCurrency)
               .orderByAsc(ThunesPayer::getName);
        
        return list(wrapper);
    }

    @Override
    public ThunesPayer convertFromDto(PayerDto payerDto) {
        if (payerDto == null) {
            return null;
        }

        ThunesPayer thunesPayer = new ThunesPayer();
        thunesPayer.setId(payerDto.getId());
        thunesPayer.setName(payerDto.getName());
        thunesPayer.setCurrency(payerDto.getCurrency());
        thunesPayer.setCountryCode(payerDto.getCountryIsoCode());
        thunesPayer.setPrecisionValue(payerDto.getPrecision());
        thunesPayer.setIncrementValue(payerDto.getIncrement());
        thunesPayer.setMinAmount(payerDto.getMinAmount());
        thunesPayer.setMaxAmount(payerDto.getMaxAmount());

        // 处理服务信息
        if (payerDto.getService() != null) {
            thunesPayer.setServiceId(payerDto.getService().getId());
            thunesPayer.setServiceName(payerDto.getService().getName());
        }

        // 处理交易类型配置
        if (payerDto.getTransactionTypes() != null && !payerDto.getTransactionTypes().isEmpty()) {
            try {
                // 将复杂的交易类型对象转换为 JSON 存储
                Map<String, Object> transactionTypesMap = new HashMap<>();
                payerDto.getTransactionTypes().forEach((key, value) -> {
                    transactionTypesMap.put(key, JSONUtil.parseObj(JSONUtil.toJsonStr(value)));
                });
                thunesPayer.setTransactionTypesJson(transactionTypesMap);
            } catch (Exception e) {
                log.warn("转换交易类型配置失败 - PayerId: {}, 错误: {}", payerDto.getId(), e.getMessage());
                thunesPayer.setTransactionTypesJson(new HashMap<>());
            }
        }

        thunesPayer.setIsActive(true);
        thunesPayer.setLastSyncAt(LocalDateTime.now());

        return thunesPayer;
    }

    @Override
    public PayerDto convertToDto(ThunesPayer thunesPayer) {
        if (thunesPayer == null) {
            return null;
        }

        PayerDto payerDto = new PayerDto();
        payerDto.setId(thunesPayer.getId());
        payerDto.setName(thunesPayer.getName());
        payerDto.setCurrency(thunesPayer.getCurrency());
        payerDto.setCountryIsoCode(thunesPayer.getCountryCode());
        payerDto.setPrecision(thunesPayer.getPrecisionValue());
        payerDto.setIncrement(thunesPayer.getIncrementValue());
        payerDto.setMinAmount(thunesPayer.getMinAmount());
        payerDto.setMaxAmount(thunesPayer.getMaxAmount());

        // 处理服务信息
        if (thunesPayer.getServiceId() != null || thunesPayer.getServiceName() != null) {
            PayerDto.ServiceDto serviceDto = new PayerDto.ServiceDto();
            serviceDto.setId(thunesPayer.getServiceId());
            serviceDto.setName(thunesPayer.getServiceName());
            payerDto.setService(serviceDto);
        }

        // 处理交易类型配置
        if (thunesPayer.getTransactionTypesJson() != null && !thunesPayer.getTransactionTypesJson().isEmpty()) {
            try {
                Map<String, PayerDto.TransactionTypeDto> transactionTypes = new HashMap<>();
                thunesPayer.getTransactionTypesJson().forEach((key, value) -> {
                    try {
                        PayerDto.TransactionTypeDto transactionTypeDto = 
                            JSONUtil.toBean(JSONUtil.toJsonStr(value), PayerDto.TransactionTypeDto.class);
                        transactionTypes.put(key, transactionTypeDto);
                    } catch (Exception e) {
                        log.warn("转换交易类型配置失败 - PayerId: {}, TransactionType: {}, 错误: {}", 
                                thunesPayer.getId(), key, e.getMessage());
                    }
                });
                payerDto.setTransactionTypes(transactionTypes);
            } catch (Exception e) {
                log.warn("转换交易类型配置失败 - PayerId: {}, 错误: {}", thunesPayer.getId(), e.getMessage());
            }
        }

        return payerDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(List<ThunesPayer> payers) {
        if (payers == null || payers.isEmpty()) {
            return;
        }

        // 使用 MyBatis-Plus 的批量保存或更新功能
        saveOrUpdateBatch(payers, 1000); // 每批处理1000条记录
    }
}
