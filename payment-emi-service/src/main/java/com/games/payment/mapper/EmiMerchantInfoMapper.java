package com.games.payment.mapper;

import com.games.payment.vo.EmiMerchantInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * EMI 商户信息（个人+企业）Mapper接口
 *
 * @author Ticker
 * @date 2025-10-17
 */
@Mapper
public interface EmiMerchantInfoMapper {

    /**
     * 查询商户信息列表（个人+企业合并）
     *
     * @param name 名称（模糊查询）
     * @param merchantType 商户类型（1：个人，2：企业）
     * @param status 状态
     * @param enabledStatus 启用状态
     * @return 商户信息列表
     */
    List<EmiMerchantInfoVo> selectMerchantList(
            @Param("name") String name,
            @Param("merchantType") Integer merchantType,
            @Param("status") Integer status,
            @Param("enabledStatus") Integer enabledStatus
    );
}


