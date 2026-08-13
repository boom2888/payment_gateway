package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.VipMapper;
import com.games.payment.domain.Vip;
import com.games.payment.service.IVipService;

import java.util.List;
import java.util.Map;

/**
 * VIPService业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements IVipService {

    /**
     * 查询VIP列表
     *
     * @param vip VIP
     * @return VIP
     */
    @Override
    public List<Vip> selectAllList(Vip vip)
    {
        return getBaseMapper().selectAllList(vip);
    }


    @Override
    public List<Vip> queryList(Vip vip) {
        LambdaQueryWrapper<Vip> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(vip.getName())){
            lqw.like(Vip::getName ,vip.getName());
        }
        if (vip.getLevel() != null){
            lqw.eq(Vip::getLevel ,vip.getLevel());
        }
        if (vip.getCreatedAt() != null){
            lqw.eq(Vip::getCreatedAt ,vip.getCreatedAt());
        }
        if (vip.getCreatedBy() != null){
            lqw.eq(Vip::getCreatedBy ,vip.getCreatedBy());
        }
        if (vip.getDeletedAt() != null){
            lqw.eq(Vip::getDeletedAt ,vip.getDeletedAt());
        }
        if (vip.getDeletedBy() != null){
            lqw.eq(Vip::getDeletedBy ,vip.getDeletedBy());
        }
        if (vip.getDeleted() != null){
            lqw.eq(Vip::getDeleted ,vip.getDeleted());
        }
        return this.list(lqw);
    }




}
