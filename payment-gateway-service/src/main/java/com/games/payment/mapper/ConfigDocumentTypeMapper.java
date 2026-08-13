package com.games.payment.mapper;

import com.games.payment.domain.ConfigDocumentType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 证件类型配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigDocumentTypeMapper extends BaseMapper<ConfigDocumentType> {
    /**
     * 查询证件类型配置列表
     *
     * @param configDocumentType 证件类型配置
     * @return 证件类型配置集合
     */
    List<ConfigDocumentType> selectAllList(ConfigDocumentType configDocumentType);

}
