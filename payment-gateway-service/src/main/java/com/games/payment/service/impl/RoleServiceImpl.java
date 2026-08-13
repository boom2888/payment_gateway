package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.RoleMapper;
import com.games.payment.domain.Role;
import com.games.payment.service.IRoleService;

import java.util.List;
import java.util.Map;

/**
 * 角色Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    /**
     * 查询角色列表
     *
     * @param role 角色
     * @return 角色
     */
    @Override
    public List<Role> selectAllList(Role role)
    {
        return getBaseMapper().selectAllList(role);
    }


    @Override
    public List<Role> queryList(Role role) {
        LambdaQueryWrapper<Role> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(role.getName())){
            lqw.like(Role::getName ,role.getName());
        }
        if (StringUtils.isNotBlank(role.getDescription())){
            lqw.eq(Role::getDescription ,role.getDescription());
        }
        if (role.getCreatedAt() != null){
            lqw.eq(Role::getCreatedAt ,role.getCreatedAt());
        }
        if (role.getCreatedBy() != null){
            lqw.eq(Role::getCreatedBy ,role.getCreatedBy());
        }
        if (role.getDeletedAt() != null){
            lqw.eq(Role::getDeletedAt ,role.getDeletedAt());
        }
        if (role.getDeletedBy() != null){
            lqw.eq(Role::getDeletedBy ,role.getDeletedBy());
        }
        return this.list(lqw);
    }




}
