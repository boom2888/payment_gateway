package com.games.payment.service;

import com.games.payment.domain.UploadCardRecord;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 上传卡记录Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IUploadCardRecordService extends IService<UploadCardRecord> {

    /**
     * 查询上传卡记录列表
     *
     * @param uploadCardRecord 上传卡记录
     * @return 上传卡记录集合
     */
    List<UploadCardRecord> selectAllList(UploadCardRecord uploadCardRecord);

    /**
     * 查询列表
     */
    List<UploadCardRecord> queryList(UploadCardRecord uploadCardRecord);

}
