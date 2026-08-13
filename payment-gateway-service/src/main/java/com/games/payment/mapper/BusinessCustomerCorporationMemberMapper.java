package com.games.payment.mapper;

import com.games.payment.domain.BusinessCustomerCorporationMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 企业客户公司成员Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface BusinessCustomerCorporationMemberMapper extends BaseMapper<BusinessCustomerCorporationMember> {
    /**
     * 查询企业客户公司成员列表
     *
     * @param businessCustomerCorporationMember 企业客户公司成员
     * @return 企业客户公司成员集合
     */
    List<BusinessCustomerCorporationMember> selectAllList(BusinessCustomerCorporationMember businessCustomerCorporationMember);

}
