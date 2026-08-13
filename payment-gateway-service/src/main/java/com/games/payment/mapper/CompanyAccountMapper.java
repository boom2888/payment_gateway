package com.games.payment.mapper;

import com.games.payment.domain.CompanyAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 公司账户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CompanyAccountMapper extends BaseMapper<CompanyAccount> {
    /**
     * 查询公司账户列表
     *
     * @param companyAccount 公司账户
     * @return 公司账户集合
     */
    List<CompanyAccount> selectAllList(CompanyAccount companyAccount);

}
