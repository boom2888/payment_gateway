package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.CompanyAccountMapper;
import com.games.payment.domain.CompanyAccount;
import com.games.payment.service.ICompanyAccountService;

import java.util.List;
import java.util.Map;

/**
 * 公司账户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CompanyAccountServiceImpl extends ServiceImpl<CompanyAccountMapper, CompanyAccount> implements ICompanyAccountService {

    /**
     * 查询公司账户列表
     *
     * @param companyAccount 公司账户
     * @return 公司账户
     */
    @Override
    public List<CompanyAccount> selectAllList(CompanyAccount companyAccount)
    {
        return getBaseMapper().selectAllList(companyAccount);
    }


    @Override
    public List<CompanyAccount> queryList(CompanyAccount companyAccount) {
        LambdaQueryWrapper<CompanyAccount> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(companyAccount.getAccountId())){
            lqw.eq(CompanyAccount::getAccountId ,companyAccount.getAccountId());
        }
        if (StringUtils.isNotBlank(companyAccount.getCurrency())){
            lqw.eq(CompanyAccount::getCurrency ,companyAccount.getCurrency());
        }
        if (companyAccount.getCreatedAt() != null){
            lqw.eq(CompanyAccount::getCreatedAt ,companyAccount.getCreatedAt());
        }
        if (companyAccount.getUpdatedAt() != null){
            lqw.eq(CompanyAccount::getUpdatedAt ,companyAccount.getUpdatedAt());
        }
        return this.list(lqw);
    }




}
