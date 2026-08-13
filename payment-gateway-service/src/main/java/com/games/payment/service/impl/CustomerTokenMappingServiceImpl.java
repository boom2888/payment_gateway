package com.games.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.payment.domain.CustomTokenMapping;
import com.games.payment.mapper.CustomerTokenMappingMapper;
import com.games.payment.service.ICustomerTokenMappingService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerTokenMappingServiceImpl extends ServiceImpl<CustomerTokenMappingMapper, CustomTokenMapping> implements ICustomerTokenMappingService {
    @Override
    public List<CustomTokenMapping> getByCardNumber(String cardNumber) {
        LambdaQueryWrapper<CustomTokenMapping> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CustomTokenMapping::getCardNumber, cardNumber);
        wrapper.orderByDesc(CustomTokenMapping::getCount);
        wrapper.orderByAsc(CustomTokenMapping::getLastUsedTime);
        return list(wrapper);
    }

    @Override
    public List<CustomTokenMapping> getByCardNumber(String cardNumber, int limit) {
        LambdaQueryWrapper<CustomTokenMapping> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CustomTokenMapping::getCardNumber, cardNumber);
        wrapper.orderByDesc(CustomTokenMapping::getCount);
        wrapper.orderByAsc(CustomTokenMapping::getLastUsedTime);
        if (limit > 0) {
            wrapper.last("limit " + limit);
        }
        return list(wrapper);
    }

    @Override
    public List<CustomTokenMapping> getByCardNumber(String cardNumber, String lastUsedOrderId) {
        LambdaQueryWrapper<CustomTokenMapping> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CustomTokenMapping::getCardNumber, cardNumber);
        wrapper.eq(CustomTokenMapping::getLastUsedOrderId, lastUsedOrderId);
        wrapper.orderByDesc(CustomTokenMapping::getCount);
        wrapper.orderByAsc(CustomTokenMapping::getLastUsedTime);
        return list(wrapper);
    }

    @Override
    public List<CustomTokenMapping> getByCardNumber(String cardNumber, Long customerId) {
        LambdaQueryWrapper<CustomTokenMapping> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CustomTokenMapping::getCardNumber, cardNumber);
        wrapper.eq(CustomTokenMapping::getCustomerId, customerId);
        wrapper.orderByDesc(CustomTokenMapping::getCount);
        wrapper.orderByAsc(CustomTokenMapping::getLastUsedTime);
        return list(wrapper);
    }

    @Override
    public List<CustomTokenMapping> selectAllList(CustomTokenMapping customTokenMapping) {
        return getBaseMapper().selectAllList(customTokenMapping);
    }

    @Override
    public List<CustomTokenMapping> queryList(CustomTokenMapping customTokenMapping) {
        return Collections.emptyList();
    }

    @Override
    public boolean increaseCountAndReset(Long id, int maxCount) {
        return getBaseMapper().increaseCountAndReset(id, maxCount) > 0;
    }

    @Override
    public boolean fillCustomerIdIfAbsent(String userTokenId, String paymentOptionId, String cardNumber, Long customerId) {
        if (customerId == null || StrUtil.isBlank(userTokenId) || StrUtil.isBlank(paymentOptionId)) {
            return false;
        }
        LambdaUpdateChainWrapper<CustomTokenMapping> updateWrapper = lambdaUpdate()
                .eq(CustomTokenMapping::getUserTokenId, userTokenId)
                .eq(CustomTokenMapping::getPaymentOptionId, paymentOptionId)
                .isNull(CustomTokenMapping::getCustomerId)
                .set(CustomTokenMapping::getCustomerId, customerId);
        if (StrUtil.isNotBlank(cardNumber)) {
            updateWrapper.eq(CustomTokenMapping::getCardNumber, cardNumber);
        }
        return updateWrapper.update();
    }
}
