package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.common.config.RuoYiConfig;
import com.games.common.enums.WhiteImportStatus;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.file.FileUploadUtils;
import com.games.common.utils.poi.ExcelStreamReader;
import com.games.payment.domain.WhiteUploadRecord;
import com.games.payment.domain.WhitelistedCreditCards;
import com.games.payment.mapper.WhiteUploadRecordMapper;
import com.games.payment.service.IWhiteUploadRecordService;
import com.games.payment.service.IWhitelistedCreditCardsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 白名单上传记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class WhiteUploadRecordServiceImpl extends ServiceImpl<WhiteUploadRecordMapper, WhiteUploadRecord>
        implements IWhiteUploadRecordService {

    private static final Logger log = LoggerFactory.getLogger(WhiteUploadRecordServiceImpl.class);

    @Resource
    private IWhitelistedCreditCardsService whitelistedCreditCardsService;

    /**
     * 查询白名单上传记录列表
     *
     * @param whiteUploadRecord 白名单上传记录
     * @return 白名单上传记录
     */
    @Override
    public List<WhiteUploadRecord> selectAllList(WhiteUploadRecord whiteUploadRecord) {
        return getBaseMapper().selectAllList(whiteUploadRecord);
    }

    @Override
    public List<WhiteUploadRecord> queryList(WhiteUploadRecord whiteUploadRecord) {
        LambdaQueryWrapper<WhiteUploadRecord> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(whiteUploadRecord.getOriginName())) {
            lqw.like(WhiteUploadRecord::getOriginName, whiteUploadRecord.getOriginName());
        }

        if (whiteUploadRecord.getStatus() != null) {
            lqw.eq(WhiteUploadRecord::getStatus, whiteUploadRecord.getStatus());
        }
        if (whiteUploadRecord.getCreatedAt() != null) {
            lqw.eq(WhiteUploadRecord::getCreatedAt, whiteUploadRecord.getCreatedAt());
        }
        return this.list(lqw);
    }

    @Override
    public String importWhite(MultipartFile file, Integer importType) throws Exception {
        // 先同步落盘，避免请求结束后临时文件被容器删除
        String originalFilename = file.getOriginalFilename();
        FileUploadUtils.FileInfo fileInfo = new FileUploadUtils().upload(RuoYiConfig.getUploadPath(), file);
        CompletableFuture.runAsync(() -> handleImportAsync(fileInfo, originalFilename, importType));
        return MessageUtils.message("file.upload.success.data.importing");
    }

    /**
     * 异步任务：完整处理导入
     */
    private void handleImportAsync(FileUploadUtils.FileInfo fileInfo, String originalFilename, Integer importType) {
        WhiteUploadRecord whiteUploadRecord = null;
        try {
            String fileName = fileInfo.realPath;
            String url = RuoYiConfig.getFilehost() + fileName;

            // 使用File对象的绝对路径
            String fullFilePath = fileInfo.file.getAbsolutePath();

            Date now = new Date();
            whiteUploadRecord = this.lambdaQuery()
                    .eq(WhiteUploadRecord::getOriginName, originalFilename)
                    .eq(WhiteUploadRecord::getDeleted, 0L)
                    .orderByDesc(WhiteUploadRecord::getCreatedAt)
                    .last("limit 1")
                    .one();

            if (whiteUploadRecord == null) {
                whiteUploadRecord = new WhiteUploadRecord();
            }

            whiteUploadRecord.setOriginName(originalFilename);
            whiteUploadRecord.setNewName(fileName);
            whiteUploadRecord.setFileUrl(url);
            whiteUploadRecord.setStatus(WhiteImportStatus.IMPORTING.ordinal());
            whiteUploadRecord.setImportTotalNum(0); // 先设置为0，后面更新
            whiteUploadRecord.setCreatedAt(now);
            whiteUploadRecord.setDeleted(0L);

            if (whiteUploadRecord.getId() == null) {
                save(whiteUploadRecord);
            } else {
                updateById(whiteUploadRecord);
            }

            final Long uploadId = whiteUploadRecord.getId();
            final Long recordId = whiteUploadRecord.getId();
            processImportAsync(fullFilePath, uploadId, recordId, importType);
        } catch (Exception e) {
            log.error("白名单导入任务执行失败", e);
            if (whiteUploadRecord != null && whiteUploadRecord.getId() != null) {
                whiteUploadRecord.setStatus(WhiteImportStatus.IMPORTED_FAIL.ordinal());
                whiteUploadRecord.setFinishedAt(new Date());
                updateById(whiteUploadRecord);
            }
        }
    }

    /**
     * 异步处理导入逻辑
     */
    private void processImportAsync(String fullFilePath, Long uploadId, Long recordId, Integer importType) {
        WhiteUploadRecord record = getById(recordId);
        if (record == null) {
            log.error("导入记录不存在: {}", recordId);
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            log.info("开始异步处理白名单导入，文件: {}", fullFilePath);

            // 使用流式读取，分批处理
            ExcelStreamReader<WhitelistedCreditCards> streamReader = new ExcelStreamReader<>(
                    WhitelistedCreditCards.class, 10000);

            final int[] totalCount = { 0 };

            // 从磁盘文件读取
            try (java.io.FileInputStream fis = new java.io.FileInputStream(fullFilePath)) {
                int totalRows = streamReader.readExcel(fis, batch -> {
                    // 设置uploadId
                    batch.forEach(data -> data.setWhiteUploadId(uploadId));

                    // 分批导入
                    whitelistedCreditCardsService.importData(batch, importType, record);

                    totalCount[0] += batch.size();
                    log.info("已处理 {} 条数据", totalCount[0]);
                });

                // 更新总数和状态为成功
                record.setImportTotalNum(totalRows);
                record.setStatus(WhiteImportStatus.IMPORTED.ordinal());
                record.setFinishedAt(new Date());
                updateById(record);

                long duration = System.currentTimeMillis() - startTime;
                log.info("白名单导入完成: 成功处理 {} 条数据，耗时 {} ms", totalRows, duration);
            }
        } catch (Exception e) {
            log.error("白名单导入失败", e);
            // 更新状态为失败
            record.setStatus(WhiteImportStatus.IMPORTED_FAIL.ordinal());
            record.setFinishedAt(new Date());
            updateById(record);
            long duration = System.currentTimeMillis() - startTime;
            log.info("白名单导入失败，累计处理 {} 条数据，耗时 {} ms", record.getImportTotalNum(), duration);
        }
    }

}
