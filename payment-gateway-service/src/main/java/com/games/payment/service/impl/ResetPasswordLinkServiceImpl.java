package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ResetPasswordLinkMapper;
import com.games.payment.domain.ResetPasswordLink;
import com.games.payment.service.IResetPasswordLinkService;

import java.util.List;
import java.util.Map;

/**
 * 重置密码链接Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ResetPasswordLinkServiceImpl extends ServiceImpl<ResetPasswordLinkMapper, ResetPasswordLink> implements IResetPasswordLinkService {

    /**
     * 查询重置密码链接列表
     *
     * @param resetPasswordLink 重置密码链接
     * @return 重置密码链接
     */
    @Override
    public List<ResetPasswordLink> selectAllList(ResetPasswordLink resetPasswordLink)
    {
        return getBaseMapper().selectAllList(resetPasswordLink);
    }


    @Override
    public List<ResetPasswordLink> queryList(ResetPasswordLink resetPasswordLink) {
        LambdaQueryWrapper<ResetPasswordLink> lqw = Wrappers.lambdaQuery();
        if (resetPasswordLink.getUserId() != null){
            lqw.eq(ResetPasswordLink::getUserId ,resetPasswordLink.getUserId());
        }
        if (StringUtils.isNotBlank(resetPasswordLink.getLink())){
            lqw.eq(ResetPasswordLink::getLink ,resetPasswordLink.getLink());
        }
        if (resetPasswordLink.getUsed() != null){
            lqw.eq(ResetPasswordLink::getUsed ,resetPasswordLink.getUsed());
        }
        if (resetPasswordLink.getCreatedAt() != null){
            lqw.eq(ResetPasswordLink::getCreatedAt ,resetPasswordLink.getCreatedAt());
        }
        if (resetPasswordLink.getCreatedBy() != null){
            lqw.eq(ResetPasswordLink::getCreatedBy ,resetPasswordLink.getCreatedBy());
        }
        if (resetPasswordLink.getDeletedAt() != null){
            lqw.eq(ResetPasswordLink::getDeletedAt ,resetPasswordLink.getDeletedAt());
        }
        if (resetPasswordLink.getDeletedBy() != null){
            lqw.eq(ResetPasswordLink::getDeletedBy ,resetPasswordLink.getDeletedBy());
        }
        if (resetPasswordLink.getDeleted() != null){
            lqw.eq(ResetPasswordLink::getDeleted ,resetPasswordLink.getDeleted());
        }
        return this.list(lqw);
    }




}
