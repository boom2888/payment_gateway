package com.games.system.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;

import cn.hutool.core.util.StrUtil;
import com.games.common.annotation.DataSource;
import com.games.common.constant.Constants;
import com.games.common.constant.Shift4Constants;
import com.games.common.constant.UserConstants;
import com.games.common.enums.YesNo;
import com.games.common.exception.CustomException;
import com.games.common.utils.StringUtils;
import com.games.system.domain.SysConfig;
import com.games.system.mapper.SysConfigMapper;
import com.games.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.games.common.core.redis.RedisCache;
import com.games.common.core.text.Convert;
import com.games.common.enums.DataSourceType;

/**
 * 参数配置 服务层实现
 * 
 * @author lor
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    @Override
    public boolean ipSwitchOpen() {
        try {
            String IP_SWITCH = selectConfigByKey(Constants.IP_SWITCH);
            if (StrUtil.isNotBlank(IP_SWITCH)) {
                return StrUtil.equals(IP_SWITCH, YesNo.YES.getValue());
            }
        } catch (Exception e) {
            log.error("获取IP修正开关配置失败, configKey: {}, 错误信息: {}", Constants.IP_SWITCH, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isUserPaymentOptionOpen() {
        try {
            String value = selectConfigByKey(Constants.USER_PAYMENT_OPTION);
            if (StrUtil.isNotBlank(value)) {
                return StrUtil.equals(value, YesNo.YES.getValue());
            }
        } catch (Exception e) {
            log.error("获取卡ID使用开关配置失败, configKey: {}, 错误信息: {}", Constants.USER_PAYMENT_OPTION, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isShopLimitOpen() {
        try {
            String value = selectConfigByKey(Constants.SHOP_LIMIT_SWITCH);
            if (StrUtil.isNotBlank(value)) {
                return StrUtil.equals(value, YesNo.YES.getValue());
            }
        } catch (Exception e) {
            log.error("获取商户每日限额开关配置失败, configKey: {}, 错误信息: {}", Constants.SHOP_LIMIT_SWITCH, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public int getMaxTokenReuse() {
        try {
            String value = selectConfigByKey(Constants.MAX_TOKEN_REUSE);
            if (StrUtil.isNotBlank(value)) {
                return Integer.parseInt(value);
            }
        } catch (Exception e) {
            log.error("获取Token最大复用次数配置失败, configKey: {}, 错误信息: {}", Constants.MAX_TOKEN_REUSE, e.getMessage(), e);
        }
        return 10; // 默认值10
    }

    @Override
    public int getMaxMappingCount() {
        try {
            String value = selectConfigByKey(Constants.MAX_MAPPING_COUNT);
            if (StrUtil.isNotBlank(value)) {
                return Integer.parseInt(value);
            }
        } catch (Exception e) {
            log.error("获取“卡复用TokenMapping存储的数量”配置失败, configKey: {}, 错误信息: {}", Constants.MAX_MAPPING_COUNT, e.getMessage(), e);
        }
        return 3;
    }

    @Override
    public int getLimitAmount() {
        try {
            String value = selectConfigByKey(Constants.LIMIT_AMOUNT);
            if (StrUtil.isNotBlank(value)) {
                return Integer.parseInt(value);
            }
        } catch (Exception e) {
            log.error("获取“7天累计支付金额限制”配置失败, configKey: {}, 错误信息: {}", Constants.MAX_MAPPING_COUNT, e.getMessage(), e);
        }
        return 1000;
    }

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<Double> getConfigByDoubleList(String configKey) {
        List<Double> values = StrUtil.split(selectConfigByKey(configKey), ",").stream().filter(StrUtil::isNotBlank)
                .map(Double::parseDouble).collect(Collectors.toList());
        return values;
    }

    @Override
    public int updateConfigValue(String configKey, String value) {
        configMapper.update(configKey, value);
        redisCache.setCacheObject(getCacheKey(configKey), value);
        return 0;
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        int row = configMapper.insertConfig(config);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        int row = configMapper.updateConfig(config);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     * 
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new CustomException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
        }
        int count = configMapper.deleteConfigByIds(configIds);
        if (count > 0) {
            Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
            redisCache.deleteObject(keys);
        }
        return count;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return Constants.SYS_CONFIG_KEY + configKey;
    }

	@Override
	public boolean shift4SaleWithTokenIsOpen() {
		String key = Shift4Constants.SHIFT4_SALE_WITH_TOKEN;
		try {
			String value = selectConfigByKey(key);
			if (StrUtil.isNotBlank(value)) {
				return StrUtil.equals(value, YesNo.YES.getValue());
			}
		} catch (Exception e) {
			log.error("获取卡ID使用开关配置失败, configKey: {}, 错误信息: {}", key, e.getMessage(), e);
		}
		return false;
	}

	@Override
	public int getShift4MaxMappingCount() {
		String key = Shift4Constants.SHIFT4_MAX_MAPPING_COUNT;
		try {
			String value = selectConfigByKey(key);
			if (StrUtil.isNotBlank(value)) {
				return Integer.parseInt(value);
			}
		} catch (Exception e) {
			log.error("获取“卡复用TokenMapping存储的数量”配置失败, configKey: {}, 错误信息: {}", key, e.getMessage(), e);
		}
		return 3;
	}

}
