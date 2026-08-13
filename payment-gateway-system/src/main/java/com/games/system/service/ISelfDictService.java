package com.games.system.service;

import com.games.common.core.domain.entity.SysDictData;

import java.util.List;

/**
 * 业务扩展自定义字典服务类
 */
public interface ISelfDictService {
    List<SysDictData> selectDictDataByType(String dictType);
}
