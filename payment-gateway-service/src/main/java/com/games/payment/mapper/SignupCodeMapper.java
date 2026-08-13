package com.games.payment.mapper;

import com.games.payment.domain.SignupCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 注册码Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SignupCodeMapper extends BaseMapper<SignupCode> {
    /**
     * 查询注册码列表
     *
     * @param signupCode 注册码
     * @return 注册码集合
     */
    List<SignupCode> selectAllList(SignupCode signupCode);

}
