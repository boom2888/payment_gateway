package com.games.payment.service;

import com.games.payment.domain.Documents;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 文档Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IDocumentsService extends IService<Documents> {

    /**
     * 查询文档列表
     *
     * @param documents 文档
     * @return 文档集合
     */
    List<Documents> selectAllList(Documents documents);

    /**
     * 查询列表
     */
    List<Documents> queryList(Documents documents);

}
