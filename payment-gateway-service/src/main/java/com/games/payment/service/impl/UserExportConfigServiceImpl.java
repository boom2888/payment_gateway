package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.payment.domain.UserExportConfig;
import com.games.payment.mapper.UserExportConfigMapper;
import com.games.payment.service.IUserExportConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户导出配置Service业务层处理
 *
 * @author System
 * @date 2025-01-03
 */
@Slf4j
@Service
public class UserExportConfigServiceImpl extends ServiceImpl<UserExportConfigMapper, UserExportConfig>
        implements IUserExportConfigService {

    @Override
    public String getUserExportConfig(Long userId, String module) {
        if (userId == null || module == null || module.trim().isEmpty()) {
            return null;
        }

        LambdaQueryWrapper<UserExportConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExportConfig::getUserId, userId)
                .eq(UserExportConfig::getModule, module);

        UserExportConfig config = this.getOne(queryWrapper);
        return config != null ? config.getExportFields() : null;
    }

    @Override
    public boolean saveUserExportConfig(Long userId, String module, String exportFields) {
        if (userId == null || module == null || module.trim().isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<UserExportConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExportConfig::getUserId, userId)
                .eq(UserExportConfig::getModule, module);

        UserExportConfig existingConfig = this.getOne(queryWrapper);

        if (existingConfig != null) {
            // 更新现有配置
            existingConfig.setExportFields(exportFields);
            return this.updateById(existingConfig);
        } else {
            // 创建新配置
            UserExportConfig newConfig = new UserExportConfig();
            newConfig.setUserId(userId);
            newConfig.setModule(module);
            newConfig.setExportFields(exportFields);
            return this.save(newConfig);
        }
    }
}

