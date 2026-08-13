package com.games.system.service.impl;

import com.games.common.constant.UserConstants;
import com.games.common.core.domain.entity.SysDictData;
import com.games.common.core.domain.entity.SysDictType;
import com.games.common.exception.CustomException;
import com.games.common.utils.DictUtils;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.StringUtils;
import com.games.system.mapper.SysDictDataMapper;
import com.games.system.mapper.SysDictTypeMapper;
import com.games.system.service.ISelfDictService;
import com.games.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author lor
 */
@Service
@Slf4j
public class SysDictTypeServiceImpl implements ISysDictTypeService
{
    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        List<SysDictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
        for (SysDictType dictType : dictTypeList)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType)
    {
        return dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll()
    {
        return dictTypeMapper.selectDictTypeAll();
    }

    @Autowired
    private ISelfDictService selfDictService;
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @param lang
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType, String lang)
    {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);

        // 如果缓存中没有数据，则从数据库加载
        if (StringUtils.isEmpty(dictDatas))
        {
            dictDatas = selfDictService.selectDictDataByType(dictType);
            // 如果selfDictService没有数据，则直接从mapper查询
            if (StringUtils.isEmpty(dictDatas)) {
                dictDatas = dictDataMapper.selectDictDataByType(dictType);
            }

            // 将数据存入缓存
            if (StringUtils.isNotEmpty(dictDatas)) {
                DictUtils.setDictCache(dictType, dictDatas);
            }
        }

        // 如果语言是中文，设置dictLabel字段
        if (StringUtils.isNotEmpty(dictDatas) && "zh".equals(lang)) {
            for (SysDictData sysDictData : dictDatas) {
                if (sysDictData.getDictLabelZh() != null) {
                    sysDictData.setDictLabel(sysDictData.getDictLabelZh());
                }

            }
        }
        return StringUtils.isNotEmpty(dictDatas) ? dictDatas : new ArrayList<>();
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId)
    {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType)
    {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    @Override
    public int deleteDictTypeByIds(Long[] dictIds)
    {
        for (Long dictId : dictIds)
        {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0)
            {
                throw new CustomException(MessageUtils.message("dict.type.assigned.cannot.delete", dictType.getDictName()));
            }
        }
        int count = dictTypeMapper.deleteDictTypeByIds(dictIds);
        if (count > 0)
        {
            DictUtils.clearDictCache();
        }
        return count;
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache()
    {
        DictUtils.clearDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dictType)
    {
        int row = dictTypeMapper.insertDictType(dictType);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(SysDictType dictType)
    {
        SysDictType oldDict = dictTypeMapper.selectDictTypeById(dictType.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
        int row = dictTypeMapper.updateDictType(dictType);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict)
    {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
