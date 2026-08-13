package com.games.payment.mapper;

import com.games.payment.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 用户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    List<User> selectAllList(User user);

}
