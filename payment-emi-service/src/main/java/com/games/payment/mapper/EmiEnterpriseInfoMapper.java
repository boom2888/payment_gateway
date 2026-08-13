package com.games.payment.mapper;

import com.games.payment.domain.EmiEnterpriseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 企业基本信息及出口业务信息Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiEnterpriseInfoMapper extends BaseMapper<EmiEnterpriseInfo> {
    /**
     * 查询企业基本信息及出口业务信息列表
     *
     * @param emiEnterpriseInfo 企业基本信息及出口业务信息
     * @return 企业基本信息及出口业务信息集合
     */
    List<EmiEnterpriseInfo> selectAllList(EmiEnterpriseInfo emiEnterpriseInfo);

    @Select("select * from emi_enterprise_info where user_id = #{userId}")
    EmiEnterpriseInfo selectByOne(Long userId);
}
