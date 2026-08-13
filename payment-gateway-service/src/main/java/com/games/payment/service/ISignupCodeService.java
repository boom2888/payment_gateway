package com.games.payment.service;

import com.games.payment.domain.SignupCode;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 注册码Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISignupCodeService extends IService<SignupCode> {

    /**
     * 查询注册码列表
     *
     * @param signupCode 注册码
     * @return 注册码集合
     */
    List<SignupCode> selectAllList(SignupCode signupCode);

    /**
     * 查询列表
     */
    List<SignupCode> queryList(SignupCode signupCode);

}
