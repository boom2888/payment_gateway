package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.Ubo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UboMapper extends BaseMapper<Ubo> {

    List<Ubo> selectAllList(Ubo ubo);

    /**
     * 批量新增商户认证受益人信息
     */
    void batchInsert(@Param("shopId") Long shopId, @Param("ubos") List<Ubo> ubos);

    /**
     * 根据商户ID删除受益人认证信息
     * @param shopId 商户ID
     */
    void deleteByShopId(@Param("shopId") Long shopId);

    /**
     * 根据商户ID查询受益人信息
     * @param shopId 商户ID
     */
    List<Ubo> selectByShopId(@Param("shopId") Long shopId);

}
