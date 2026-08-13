package com.games.payment.mapper;

import com.games.payment.domain.SaasUserCorporation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * SaaS用户公司Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SaasUserCorporationMapper extends BaseMapper<SaasUserCorporation> {
    /**
     * 查询SaaS用户公司列表
     *
     * @param saasUserCorporation SaaS用户公司
     * @return SaaS用户公司集合
     */
    List<SaasUserCorporation> selectAllList(SaasUserCorporation saasUserCorporation);

}
