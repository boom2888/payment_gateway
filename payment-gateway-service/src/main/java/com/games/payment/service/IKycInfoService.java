package com.games.payment.service;

import com.games.payment.domain.KycInfo;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * KYC信息Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IKycInfoService extends IService<KycInfo> {

    /**
     * 查询KYC信息列表
     *
     * @param kycInfo KYC信息
     * @return KYC信息集合
     */
    List<KycInfo> selectAllList(KycInfo kycInfo);

    /**
     * 查询列表
     */
    List<KycInfo> queryList(KycInfo kycInfo);

}
