package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.WhiteUploadRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 白名单上传记录Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IWhiteUploadRecordService extends IService<WhiteUploadRecord> {

    /**
     * 查询白名单上传记录列表
     *
     * @param whiteUploadRecord 白名单上传记录
     * @return 白名单上传记录集合
     */
    List<WhiteUploadRecord> selectAllList(WhiteUploadRecord whiteUploadRecord);

    /**
     * 查询列表
     */
    List<WhiteUploadRecord> queryList(WhiteUploadRecord whiteUploadRecord);

    String importWhite(MultipartFile file, Integer importType) throws Exception;
}
