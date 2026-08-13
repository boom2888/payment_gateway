package com.games.payment.service;

import com.games.common.core.domain.AjaxResult;
import com.games.payment.domain.MovementImport;
import com.baomidou.mybatisplus.spring.service.IService;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * nuvei报告导入记录Service接口
 *
 * @author Ticker
 * @date 2025-07-21
 */
public interface IMovementImportService extends IService<MovementImport> {

    /**
     * 查询nuvei报告导入记录列表
     *
     * @param movementImport nuvei报告导入记录
     * @return nuvei报告导入记录集合
     */
    List<MovementImport> selectAllList(MovementImport movementImport);

    /**
     * 查询列表
     */
    List<MovementImport> queryList(MovementImport movementImport);

    String importNuvieReport(MultipartFile file);

    byte[] exportReconReport(Long movementImportId) throws IOException;

    String exportDailySummary(List<String> movementImportId) throws IOException;

    boolean invalid(Long movementImportId);
}
