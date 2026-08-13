package com.games.payment.service.persistence;

import com.games.common.enums.MovementImportStatus;
import com.games.common.exception.CustomException;
import com.games.common.utils.MessageUtils;
import com.games.payment.domain.MovementImport;
import com.games.payment.mapper.MovementImportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Date;

/**
 * Transactional persistence helper for MovementImport records.
 */
@Slf4j
@Service
public class MovementImportPersistenceService {

    @Resource
    private MovementImportMapper movementImportMapper;

    @Transactional(rollbackFor = Exception.class)
    public MovementImport saveOrUpdateImportRecord(String originalFileName, String newFileName, String fileUrl,
            int totalNum) {
        MovementImport existingImport = movementImportMapper.selectByOriginNameIncludeDeleted(originalFileName);

        MovementImport movementImport;
        if (existingImport != null) {
            if (existingImport.getDeleted() == null || existingImport.getDeleted() == 0L) {
                throw new CustomException(MessageUtils.message("file.name.already.exists", originalFileName));
            }

            movementImport = existingImport;
            log.info("检测到已删除的记录，将恢复并重新导入，报告ID: {}", existingImport.getId());

            movementImport.setNewName(newFileName);
            movementImport.setFileUrl(fileUrl);
            movementImport.setStatus(MovementImportStatus.GENERATING.ordinal());
            movementImport.setImportTotalNum(totalNum);
            movementImport.setCreatedAt(new Date());
            movementImport.setRemark(null);
            movementImport.setDeleted(0L);
            movementImport.setDeletedAt(null);
            movementImport.setDeletedBy(null);

            int updateRows = movementImportMapper.updateByIdIgnoreDeleted(movementImport);
            if (updateRows == 0) {
                log.error("更新 MovementImport 记录失败，报告ID: {}, 文件名: {}", movementImport.getId(), originalFileName);
                throw new CustomException(MessageUtils.message("movement.import.update.failed"));
            }
            log.info("更新 MovementImport 记录成功，报告ID: {}，更新 {} 行", movementImport.getId(), updateRows);
        } else {
            movementImport = new MovementImport();
            movementImport.setOriginName(originalFileName);
            movementImport.setNewName(newFileName);
            movementImport.setFileUrl(fileUrl);
            movementImport.setStatus(MovementImportStatus.GENERATING.ordinal());
            movementImport.setImportTotalNum(totalNum);
            movementImport.setCreatedAt(new Date());
            movementImport.setDeleted(0L);

            boolean saveSuccess = movementImportMapper.insert(movementImport) > 0;
            if (!saveSuccess) {
                log.error("保存 MovementImport 记录失败，文件名: {}", originalFileName);
                throw new CustomException(MessageUtils.message("movement.import.save.failed"));
            }
            log.info("保存 MovementImport 记录成功，报告ID: {}", movementImport.getId());
        }

        return movementImport;
    }
}
