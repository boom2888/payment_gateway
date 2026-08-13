package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.CustomTokenMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerTokenMappingMapper extends BaseMapper<CustomTokenMapping> {

    List<CustomTokenMapping> selectAllList(CustomTokenMapping customer);

    int increaseCountAndReset(@Param("id") Long id, @Param("maxCount") int maxCount);
}
