package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.MerchantPayoutIpMapper;
import com.games.payment.domain.MerchantPayoutIp;
import com.games.payment.service.IMerchantPayoutIpService;

import java.util.List;
import java.util.Map;

/**
 * 商户支付IPService业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class MerchantPayoutIpServiceImpl extends ServiceImpl<MerchantPayoutIpMapper, MerchantPayoutIp> implements IMerchantPayoutIpService {

    /**
     * 查询商户支付IP列表
     *
     * @param merchantPayoutIp 商户支付IP
     * @return 商户支付IP
     */
    @Override
    public List<MerchantPayoutIp> selectAllList(MerchantPayoutIp merchantPayoutIp)
    {
        return getBaseMapper().selectAllList(merchantPayoutIp);
    }


    @Override
    public List<MerchantPayoutIp> queryList(MerchantPayoutIp merchantPayoutIp) {
        LambdaQueryWrapper<MerchantPayoutIp> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(merchantPayoutIp.getMerchantId())){
            lqw.eq(MerchantPayoutIp::getMerchantId ,merchantPayoutIp.getMerchantId());
        }
        if (StringUtils.isNotBlank(merchantPayoutIp.getPayoutIps())){
            lqw.eq(MerchantPayoutIp::getPayoutIps ,merchantPayoutIp.getPayoutIps());
        }
        if (merchantPayoutIp.getCreatedAt() != null){
            lqw.eq(MerchantPayoutIp::getCreatedAt ,merchantPayoutIp.getCreatedAt());
        }
        if (merchantPayoutIp.getUpdatedAt() != null){
            lqw.eq(MerchantPayoutIp::getUpdatedAt ,merchantPayoutIp.getUpdatedAt());
        }
        return this.list(lqw);
    }




}
