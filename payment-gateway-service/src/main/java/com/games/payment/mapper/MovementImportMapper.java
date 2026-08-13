package com.games.payment.mapper;

import com.games.payment.domain.MovementImport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * nuvei报告导入记录Mapper接口
 *
 * @author Ticker
 * @date 2025-07-21
 */
public interface MovementImportMapper extends BaseMapper<MovementImport> {
    /**
     * 查询nuvei报告导入记录列表
     *
     * @param movementImport nuvei报告导入记录
     * @return nuvei报告导入记录集合
     */
    List<MovementImport> selectAllList(MovementImport movementImport);

    int invalid(Long movementImportId);

    /**
     * 根据原始文件名查询记录（包括已删除的）
     *
     * @param originName 原始文件名
     * @return 记录
     */
    MovementImport selectByOriginNameIncludeDeleted(String originName);

    /**
     * 更新记录（忽略逻辑删除）
     *
     * @param movementImport 记录
     * @return 更新的行数
     */
    int updateByIdIgnoreDeleted(MovementImport movementImport);
}
