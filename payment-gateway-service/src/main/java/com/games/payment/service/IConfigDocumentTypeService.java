package com.games.payment.service;

import com.games.payment.domain.ConfigDocumentType;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 证件类型配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigDocumentTypeService extends IService<ConfigDocumentType> {

    /**
     * 查询证件类型配置列表
     *
     * @param configDocumentType 证件类型配置
     * @return 证件类型配置集合
     */
    List<ConfigDocumentType> selectAllList(ConfigDocumentType configDocumentType);

    /**
     * 查询列表
     */
    List<ConfigDocumentType> queryList(ConfigDocumentType configDocumentType);

}
