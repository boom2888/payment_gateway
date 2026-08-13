package com.games.payment.mapper;

import com.games.payment.domain.Documents;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 文档Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface DocumentsMapper extends BaseMapper<Documents> {
    /**
     * 查询文档列表
     *
     * @param documents 文档
     * @return 文档集合
     */
    List<Documents> selectAllList(Documents documents);

}
