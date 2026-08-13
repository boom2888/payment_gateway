package com.games.payment.mapper;

import com.games.payment.domain.KycInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * KYC信息Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface KycInfoMapper extends BaseMapper<KycInfo> {
    /**
     * 查询KYC信息列表
     *
     * @param kycInfo KYC信息
     * @return KYC信息集合
     */
    List<KycInfo> selectAllList(KycInfo kycInfo);

}
