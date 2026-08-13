package com.games.payment.mapper;

import com.games.payment.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 角色Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询角色列表
     *
     * @param role 角色
     * @return 角色集合
     */
    List<Role> selectAllList(Role role);

}
