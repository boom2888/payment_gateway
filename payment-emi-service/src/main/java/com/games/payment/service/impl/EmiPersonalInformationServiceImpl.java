package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.EmiPersonalInformationMapper;
import com.games.payment.domain.EmiPersonalInformation;
import com.games.payment.service.IEmiPersonalInformationService;

import java.util.List;
import java.util.Map;

/**
 * 个人信息Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Service
public class EmiPersonalInformationServiceImpl extends ServiceImpl<EmiPersonalInformationMapper, EmiPersonalInformation> implements IEmiPersonalInformationService {

    /**
     * 查询个人信息列表
     *
     * @param emiPersonalInformation 个人信息
     * @return 个人信息
     */
    @Override
    public List<EmiPersonalInformation> selectAllList(EmiPersonalInformation emiPersonalInformation)
    {
        return getBaseMapper().selectAllList(emiPersonalInformation);
    }


    @Override
    public List<EmiPersonalInformation> queryList(EmiPersonalInformation emiPersonalInformation) {
        LambdaQueryWrapper<EmiPersonalInformation> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(emiPersonalInformation.getAccountType())){
            lqw.eq(EmiPersonalInformation::getAccountType ,emiPersonalInformation.getAccountType());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getNationality())){
            lqw.eq(EmiPersonalInformation::getNationality ,emiPersonalInformation.getNationality());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getEmail())){
            lqw.eq(EmiPersonalInformation::getEmail ,emiPersonalInformation.getEmail());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getIdType())){
            lqw.eq(EmiPersonalInformation::getIdType ,emiPersonalInformation.getIdType());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getIdPhoto())){
            lqw.eq(EmiPersonalInformation::getIdPhoto ,emiPersonalInformation.getIdPhoto());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getFullName())){
            lqw.like(EmiPersonalInformation::getFullName ,emiPersonalInformation.getFullName());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getIdNumber())){
            lqw.eq(EmiPersonalInformation::getIdNumber ,emiPersonalInformation.getIdNumber());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getGender())){
            lqw.eq(EmiPersonalInformation::getGender ,emiPersonalInformation.getGender());
        }
        if (emiPersonalInformation.getBirthDate() != null){
            lqw.eq(EmiPersonalInformation::getBirthDate ,emiPersonalInformation.getBirthDate());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getResidenceCountry())){
            lqw.eq(EmiPersonalInformation::getResidenceCountry ,emiPersonalInformation.getResidenceCountry());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getResidenceProvinceCity())){
            lqw.eq(EmiPersonalInformation::getResidenceProvinceCity ,emiPersonalInformation.getResidenceProvinceCity());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getResidenceAddress())){
            lqw.eq(EmiPersonalInformation::getResidenceAddress ,emiPersonalInformation.getResidenceAddress());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getResidencePostalCode())){
            lqw.eq(EmiPersonalInformation::getResidencePostalCode ,emiPersonalInformation.getResidencePostalCode());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getContactPhone())){
            lqw.eq(EmiPersonalInformation::getContactPhone ,emiPersonalInformation.getContactPhone());
        }
        if (StringUtils.isNotBlank(emiPersonalInformation.getFundSource())){
            lqw.eq(EmiPersonalInformation::getFundSource ,emiPersonalInformation.getFundSource());
        }
        if (emiPersonalInformation.getCreatedAt() != null){
            lqw.eq(EmiPersonalInformation::getCreatedAt ,emiPersonalInformation.getCreatedAt());
        }
        if (emiPersonalInformation.getUpdatedAt() != null){
            lqw.eq(EmiPersonalInformation::getUpdatedAt ,emiPersonalInformation.getUpdatedAt());
        }
        if (emiPersonalInformation.getUserId() != null){
            lqw.eq(EmiPersonalInformation::getUserId ,emiPersonalInformation.getUserId());
        }
        return this.list(lqw);
    }

    /**
     * 根据用户ID查询个人信息
     *
     * @param userId 用户ID
     * @return 个人信息
     */
    @Override
    public EmiPersonalInformation getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        LambdaQueryWrapper<EmiPersonalInformation> lqw = Wrappers.lambdaQuery();
        lqw.eq(EmiPersonalInformation::getUserId, userId);
        return this.getOne(lqw);
    }

}
