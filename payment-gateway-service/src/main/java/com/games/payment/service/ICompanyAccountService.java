package com.games.payment.service;

import com.games.payment.domain.CompanyAccount;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 公司账户Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICompanyAccountService extends IService<CompanyAccount> {

    /**
     * 查询公司账户列表
     *
     * @param companyAccount 公司账户
     * @return 公司账户集合
     */
    List<CompanyAccount> selectAllList(CompanyAccount companyAccount);

    /**
     * 查询列表
     */
    List<CompanyAccount> queryList(CompanyAccount companyAccount);

}
