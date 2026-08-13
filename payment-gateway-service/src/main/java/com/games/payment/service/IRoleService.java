package com.games.payment.service;

import com.games.payment.domain.Role;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 角色Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查询角色列表
     *
     * @param role 角色
     * @return 角色集合
     */
    List<Role> selectAllList(Role role);

    /**
     * 查询列表
     */
    List<Role> queryList(Role role);

}
