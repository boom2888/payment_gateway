package com.games.payment.service;

import com.games.payment.domain.MfaCode;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * MFA代码Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IMfaCodeService extends IService<MfaCode> {

    /**
     * 查询MFA代码列表
     *
     * @param mfaCode MFA代码
     * @return MFA代码集合
     */
    List<MfaCode> selectAllList(MfaCode mfaCode);

    /**
     * 查询列表
     */
    List<MfaCode> queryList(MfaCode mfaCode);

}
