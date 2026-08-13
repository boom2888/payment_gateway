package com.games.payment.service;

import com.games.payment.domain.User;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IUserService extends IService<User> {

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    List<User> selectAllList(User user);

    /**
     * 查询列表
     */
    List<User> queryList(User user);

}
