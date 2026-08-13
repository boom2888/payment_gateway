package com.games.payment.service;

import com.games.payment.domain.EmiPersonalInformation;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 个人信息Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiPersonalInformationService extends IService<EmiPersonalInformation> {

    /**
     * 查询个人信息列表
     *
     * @param emiPersonalInformation 个人信息
     * @return 个人信息集合
     */
    List<EmiPersonalInformation> selectAllList(EmiPersonalInformation emiPersonalInformation);

    /**
     * 查询列表
     */
    List<EmiPersonalInformation> queryList(EmiPersonalInformation emiPersonalInformation);

    /**
     * 根据用户ID查询个人信息
     *
     * @param userId 用户ID
     * @return 个人信息
     */
    EmiPersonalInformation getByUserId(Long userId);

}
