package com.games.payment.service;

import com.games.payment.domain.ConfigCheckoutErrorMapping;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * Checkout错误映射配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigCheckoutErrorMappingService extends IService<ConfigCheckoutErrorMapping> {

    /**
     * 查询Checkout错误映射配置列表
     *
     * @param configCheckoutErrorMapping Checkout错误映射配置
     * @return Checkout错误映射配置集合
     */
    List<ConfigCheckoutErrorMapping> selectAllList(ConfigCheckoutErrorMapping configCheckoutErrorMapping);

    /**
     * 查询列表
     */
    List<ConfigCheckoutErrorMapping> queryList(ConfigCheckoutErrorMapping configCheckoutErrorMapping);

}
