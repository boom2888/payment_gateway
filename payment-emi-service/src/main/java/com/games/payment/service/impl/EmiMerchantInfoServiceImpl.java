package com.games.payment.service.impl;

import com.games.payment.mapper.EmiMerchantInfoMapper;
import com.games.payment.service.IEmiMerchantInfoService;
import com.games.payment.vo.EmiMerchantInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EMI 商户信息（个人+企业）Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-17
 */
@Slf4j
@Service
public class EmiMerchantInfoServiceImpl implements IEmiMerchantInfoService {

    @Autowired
    private EmiMerchantInfoMapper emiMerchantInfoMapper;

    /**
     * 查询商户信息列表（个人+企业合并）
     *
     * @param name 名称（模糊查询）
     * @param merchantType 商户类型（1：个人，2：企业）
     * @param status 状态
     * @param enabledStatus 启用状态
     * @return 商户信息列表
     */
    @Override
    public List<EmiMerchantInfoVo> selectMerchantList(String name, Integer merchantType, Integer status, Integer enabledStatus) {
        return emiMerchantInfoMapper.selectMerchantList(name, merchantType, status, enabledStatus);
    }
}


