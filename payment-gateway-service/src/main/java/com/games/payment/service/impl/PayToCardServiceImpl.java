package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.PayToCardMapper;
import com.games.payment.domain.PayToCard;
import com.games.payment.service.IPayToCardService;

import java.util.List;
import java.util.Map;

/**
 * 支付到卡Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class PayToCardServiceImpl extends ServiceImpl<PayToCardMapper, PayToCard> implements IPayToCardService {

    /**
     * 查询支付到卡列表
     *
     * @param payToCard 支付到卡
     * @return 支付到卡
     */
    @Override
    public List<PayToCard> selectAllList(PayToCard payToCard)
    {
        return getBaseMapper().selectAllList(payToCard);
    }


    @Override
    public List<PayToCard> queryList(PayToCard payToCard) {
        LambdaQueryWrapper<PayToCard> lqw = Wrappers.lambdaQuery();
        if (payToCard.getUserId() != null){
            lqw.eq(PayToCard::getUserId ,payToCard.getUserId());
        }
        if (payToCard.getCurrencyId() != null){
            lqw.eq(PayToCard::getCurrencyId ,payToCard.getCurrencyId());
        }
        if (StringUtils.isNotBlank(payToCard.getCounterpartyName())){
            lqw.like(PayToCard::getCounterpartyName ,payToCard.getCounterpartyName());
        }
        if (StringUtils.isNotBlank(payToCard.getCounterpartyId())){
            lqw.eq(PayToCard::getCounterpartyId ,payToCard.getCounterpartyId());
        }
        if (StringUtils.isNotBlank(payToCard.getState())){
            lqw.eq(PayToCard::getState ,payToCard.getState());
        }
        if (payToCard.getCreatedAt() != null){
            lqw.eq(PayToCard::getCreatedAt ,payToCard.getCreatedAt());
        }
        if (StringUtils.isNotBlank(payToCard.getLinkId())){
            lqw.eq(PayToCard::getLinkId ,payToCard.getLinkId());
        }
        if (StringUtils.isNotBlank(payToCard.getAccountId())){
            lqw.eq(PayToCard::getAccountId ,payToCard.getAccountId());
        }
        return this.list(lqw);
    }




}
