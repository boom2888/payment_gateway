package com.games.web.controller.payment;

import com.games.common.core.page.TableDataInfo;
import com.games.payment.domain.ThunesTransferRecord;
import com.games.payment.service.IThunesTransferRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 转账记录 Controller
 * 
 * @author System
 * @date 2025-01-14
 */
@RestController
@RequestMapping("/payment/transferRecord")
@RequiredArgsConstructor
public class ThunesTransferRecordController extends BasePayController {

    private final IThunesTransferRecordService transferRecordService;

    /**
     * 查询转账记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:transferRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ThunesTransferRecord transferRecord) {
        startPage();
        List<ThunesTransferRecord> list = transferRecordService.selectList(transferRecord);
        return getDataTable(list);
    }
}
