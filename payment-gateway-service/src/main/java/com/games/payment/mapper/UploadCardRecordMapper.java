package com.games.payment.mapper;

import com.games.payment.domain.UploadCardRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 上传卡记录Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface UploadCardRecordMapper extends BaseMapper<UploadCardRecord> {
    /**
     * 查询上传卡记录列表
     *
     * @param uploadCardRecord 上传卡记录
     * @return 上传卡记录集合
     */
    List<UploadCardRecord> selectAllList(UploadCardRecord uploadCardRecord);

}
