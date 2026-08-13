package com.games.payment.mapper;

import com.games.payment.domain.ConfigCheckoutErrorMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * Checkout错误映射配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigCheckoutErrorMappingMapper extends BaseMapper<ConfigCheckoutErrorMapping> {
    /**
     * 查询Checkout错误映射配置列表
     *
     * @param configCheckoutErrorMapping Checkout错误映射配置
     * @return Checkout错误映射配置集合
     */
    List<ConfigCheckoutErrorMapping> selectAllList(ConfigCheckoutErrorMapping configCheckoutErrorMapping);

}
