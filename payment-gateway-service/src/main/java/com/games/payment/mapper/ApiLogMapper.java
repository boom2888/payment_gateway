package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.ApiLog;

import java.util.List;

/**
 * 接口日志Mapper接口
 *
 * @author ç½ä½³ç
 * @date 2024-06-03
 */
public interface ApiLogMapper extends BaseMapper<ApiLog> {
    /**
     * 查询接口日志列表
     *
     * @param ApiLog 接口日志
     * @return 接口日志集合
     */
    List<ApiLog> selectAllList(ApiLog ApiLog);


}
