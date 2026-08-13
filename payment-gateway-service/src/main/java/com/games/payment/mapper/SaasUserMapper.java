package com.games.payment.mapper;

import com.games.payment.domain.SaasUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * SaaS用户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SaasUserMapper extends BaseMapper<SaasUser> {
    /**
     * 查询SaaS用户列表
     *
     * @param saasUser SaaS用户
     * @return SaaS用户集合
     */
    List<SaasUser> selectAllList(SaasUser saasUser);

}
