package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.UserMapper;
import com.games.payment.domain.User;
import com.games.payment.service.IUserService;

import java.util.List;
import java.util.Map;

/**
 * 用户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户
     */
    @Override
    public List<User> selectAllList(User user)
    {
        return getBaseMapper().selectAllList(user);
    }


    @Override
    public List<User> queryList(User user) {
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(user.getEmail())){
            lqw.eq(User::getEmail ,user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getCountryCode())){
            lqw.eq(User::getCountryCode ,user.getCountryCode());
        }
        if (StringUtils.isNotBlank(user.getPhoneNumber())){
            lqw.eq(User::getPhoneNumber ,user.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(user.getPassword())){
            lqw.eq(User::getPassword ,user.getPassword());
        }
        if (StringUtils.isNotBlank(user.getDisplayName())){
            lqw.like(User::getDisplayName ,user.getDisplayName());
        }
        if (user.getType() != null){
            lqw.eq(User::getType ,user.getType());
        }
        if (StringUtils.isNotBlank(user.getAvatar())){
            lqw.eq(User::getAvatar ,user.getAvatar());
        }
        if (user.getMfaSetting() != null){
            lqw.eq(User::getMfaSetting ,user.getMfaSetting());
        }
        if (user.getNotificationSetting() != null){
            lqw.eq(User::getNotificationSetting ,user.getNotificationSetting());
        }
        if (user.getKycId() != null){
            lqw.eq(User::getKycId ,user.getKycId());
        }
        if (StringUtils.isNotBlank(user.getKycVeriffUuid())){
            lqw.eq(User::getKycVeriffUuid ,user.getKycVeriffUuid());
        }
        if (user.getKycStatus() != null){
            lqw.eq(User::getKycStatus ,user.getKycStatus());
        }
        if (user.getRole() != null){
            lqw.eq(User::getRole ,user.getRole());
        }
        if (user.getActivated() != null){
            lqw.eq(User::getActivated ,user.getActivated());
        }
        if (StringUtils.isNotBlank(user.getIdRef())){
            lqw.eq(User::getIdRef ,user.getIdRef());
        }
        if (user.getCreatedAt() != null){
            lqw.eq(User::getCreatedAt ,user.getCreatedAt());
        }
        if (user.getCreatedBy() != null){
            lqw.eq(User::getCreatedBy ,user.getCreatedBy());
        }
        if (user.getDeletedAt() != null){
            lqw.eq(User::getDeletedAt ,user.getDeletedAt());
        }
        if (user.getDeletedBy() != null){
            lqw.eq(User::getDeletedBy ,user.getDeletedBy());
        }
        if (user.getDeleted() != null){
            lqw.eq(User::getDeleted ,user.getDeleted());
        }
        return this.list(lqw);
    }




}
