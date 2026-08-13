package com.games.payment.mapper;

import com.games.payment.domain.MfaCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * MFA代码Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface MfaCodeMapper extends BaseMapper<MfaCode> {
    /**
     * 查询MFA代码列表
     *
     * @param mfaCode MFA代码
     * @return MFA代码集合
     */
    List<MfaCode> selectAllList(MfaCode mfaCode);

}
