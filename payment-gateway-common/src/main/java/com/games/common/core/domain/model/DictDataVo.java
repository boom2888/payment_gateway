package com.games.common.core.domain.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.games.common.annotation.Excel;
import com.games.common.core.domain.entity.SysDictData;
import com.games.common.utils.MessageUtils;
import lombok.Data;

/**
 * 字典数据表 sys_dict_data
 *
 * @author fd
 */
@Data
public class DictDataVo {
    private static final long serialVersionUID = 1L;

    public static DictDataVo change(SysDictData item) {
        DictDataVo vo = BeanUtil.copyProperties(item, DictDataVo.class);
        String key = item.getLangKey();
        if(StrUtil.isNotBlank(key)){
            vo.setDictLabel(MessageUtils.message(key));
        }
        return vo;
    }

    /**
     * 字典排序
     */
    @Excel(name = "字典排序", cellType = Excel.ColumnType.NUMERIC)
    private Long dictSort;

    /**
     * 字典标签
     */
    @Excel(name = "字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @Excel(name = "字典键值")
    private String dictValue;


}
