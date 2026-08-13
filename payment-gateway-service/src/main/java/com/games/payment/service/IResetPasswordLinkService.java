package com.games.payment.service;

import com.games.payment.domain.ResetPasswordLink;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 重置密码链接Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IResetPasswordLinkService extends IService<ResetPasswordLink> {

    /**
     * 查询重置密码链接列表
     *
     * @param resetPasswordLink 重置密码链接
     * @return 重置密码链接集合
     */
    List<ResetPasswordLink> selectAllList(ResetPasswordLink resetPasswordLink);

    /**
     * 查询列表
     */
    List<ResetPasswordLink> queryList(ResetPasswordLink resetPasswordLink);

}
