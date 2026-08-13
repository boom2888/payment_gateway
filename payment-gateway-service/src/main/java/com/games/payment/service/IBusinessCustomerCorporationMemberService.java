package com.games.payment.service;

import com.games.payment.domain.BusinessCustomerCorporationMember;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 企业客户公司成员Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IBusinessCustomerCorporationMemberService extends IService<BusinessCustomerCorporationMember> {

    /**
     * 查询企业客户公司成员列表
     *
     * @param businessCustomerCorporationMember 企业客户公司成员
     * @return 企业客户公司成员集合
     */
    List<BusinessCustomerCorporationMember> selectAllList(BusinessCustomerCorporationMember businessCustomerCorporationMember);

    /**
     * 查询列表
     */
    List<BusinessCustomerCorporationMember> queryList(BusinessCustomerCorporationMember businessCustomerCorporationMember);

}
