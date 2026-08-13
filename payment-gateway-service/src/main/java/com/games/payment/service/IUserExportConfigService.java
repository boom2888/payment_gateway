package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.UserExportConfig;

/**
 * 用户导出配置Service接口
 *
 * @author System
 * @date 2025-01-03
 */
public interface IUserExportConfigService extends IService<UserExportConfig> {

    /**
     * 获取用户某个模块的导出配置
     *
     * @param userId 用户ID
     * @param module 模块名称
     * @return 导出字段配置
     */
    String getUserExportConfig(Long userId, String module);

    /**
     * 保存用户某个模块的导出配置
     *
     * @param userId 用户ID
     * @param module 模块名称
     * @param exportFields 导出字段配置
     * @return 是否保存成功
     */
    boolean saveUserExportConfig(Long userId, String module, String exportFields);
}

