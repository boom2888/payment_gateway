package com.games.payment.mapper;

import com.games.payment.domain.EmiPersonalInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 个人信息Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiPersonalInformationMapper extends BaseMapper<EmiPersonalInformation> {
    /**
     * 查询个人信息列表
     *
     * @param emiPersonalInformation 个人信息
     * @return 个人信息集合
     */
    List<EmiPersonalInformation> selectAllList(EmiPersonalInformation emiPersonalInformation);

}
