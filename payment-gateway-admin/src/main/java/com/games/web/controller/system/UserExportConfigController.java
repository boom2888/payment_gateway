package com.games.web.controller.system;

import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.utils.SecurityUtils;
import com.games.payment.service.IUserExportConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户导出配置Controller
 *
 * @author System
 * @date 2025-01-03
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/system/user/exportConfig")
public class UserExportConfigController extends BaseController {

    private final IUserExportConfigService userExportConfigService;

    /**
     * 获取用户某个模块的导出配置
     *
     * @param module 模块名称
     * @return 导出字段配置
     */
    @GetMapping("/{module}")
    public AjaxResult getConfig(@PathVariable String module) {
        try {
            Long userId = SecurityUtils.getLoginUser().getUserId();
            String exportFields = userExportConfigService.getUserExportConfig(userId, module);
            return AjaxResult.success(exportFields);
        } catch (Exception e) {
            log.error("获取用户导出配置失败", e);
            return AjaxResult.error("获取配置失败");
        }
    }

    /**
     * 保存用户某个模块的导出配置
     *
     * @param module 模块名称
     * @param exportFields 导出字段配置
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult saveConfig(@RequestParam String module,
                                  @RequestParam String exportFields) {
        try {
            Long userId = SecurityUtils.getLoginUser().getUserId();
            log.info("保存用户导出配置，userId={}, module={}, exportFields={}", userId, module, exportFields);

            boolean success = userExportConfigService.saveUserExportConfig(userId, module, exportFields);
            return success ? AjaxResult.success("保存成功") : AjaxResult.error("保存失败");
        } catch (Exception e) {
            log.error("保存用户导出配置失败", e);
            return AjaxResult.error("保存配置失败");
        }
    }
}

