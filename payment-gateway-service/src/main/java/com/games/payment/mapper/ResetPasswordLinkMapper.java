package com.games.payment.mapper;

import com.games.payment.domain.ResetPasswordLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 重置密码链接Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ResetPasswordLinkMapper extends BaseMapper<ResetPasswordLink> {
    /**
     * 查询重置密码链接列表
     *
     * @param resetPasswordLink 重置密码链接
     * @return 重置密码链接集合
     */
    List<ResetPasswordLink> selectAllList(ResetPasswordLink resetPasswordLink);

}
