package com.games.payment.service;

import com.games.payment.vo.EmiMerchantInfoVo;

import java.util.List;

/**
 * EMI 商户信息（个人+企业）Service接口
 *
 * @author Ticker
 * @date 2025-10-17
 */
public interface IEmiMerchantInfoService {

    /**
     * 查询商户信息列表（个人+企业合并）
     *
     * @param name 名称（模糊查询）
     * @param merchantType 商户类型（1：个人，2：企业）
     * @param status 状态
     * @param enabledStatus 启用状态
     * @return 商户信息列表
     */
    List<EmiMerchantInfoVo> selectMerchantList(String name, Integer merchantType, Integer status, Integer enabledStatus);
}


