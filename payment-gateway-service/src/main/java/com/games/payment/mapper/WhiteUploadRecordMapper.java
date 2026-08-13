package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.WhiteUploadRecord;

import java.util.List;
/**
 * 白名单上传记录Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface WhiteUploadRecordMapper extends BaseMapper<WhiteUploadRecord> {
    /**
     * 查询白名单上传记录列表
     *
     * @param whiteUploadRecord 白名单上传记录
     * @return 白名单上传记录集合
     */
    List<WhiteUploadRecord> selectAllList(WhiteUploadRecord whiteUploadRecord);

}
