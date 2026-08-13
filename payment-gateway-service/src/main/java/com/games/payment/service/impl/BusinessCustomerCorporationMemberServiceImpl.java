package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.BusinessCustomerCorporationMemberMapper;
import com.games.payment.domain.BusinessCustomerCorporationMember;
import com.games.payment.service.IBusinessCustomerCorporationMemberService;

import java.util.List;
import java.util.Map;

/**
 * 企业客户公司成员Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class BusinessCustomerCorporationMemberServiceImpl extends ServiceImpl<BusinessCustomerCorporationMemberMapper, BusinessCustomerCorporationMember> implements IBusinessCustomerCorporationMemberService {

    /**
     * 查询企业客户公司成员列表
     *
     * @param businessCustomerCorporationMember 企业客户公司成员
     * @return 企业客户公司成员
     */
    @Override
    public List<BusinessCustomerCorporationMember> selectAllList(BusinessCustomerCorporationMember businessCustomerCorporationMember)
    {
        return getBaseMapper().selectAllList(businessCustomerCorporationMember);
    }


    @Override
    public List<BusinessCustomerCorporationMember> queryList(BusinessCustomerCorporationMember businessCustomerCorporationMember) {
        LambdaQueryWrapper<BusinessCustomerCorporationMember> lqw = Wrappers.lambdaQuery();
        if (businessCustomerCorporationMember.getBusinessCustomerCorporationId() != null){
            lqw.eq(BusinessCustomerCorporationMember::getBusinessCustomerCorporationId ,businessCustomerCorporationMember.getBusinessCustomerCorporationId());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getFirstName())){
            lqw.like(BusinessCustomerCorporationMember::getFirstName ,businessCustomerCorporationMember.getFirstName());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getMiddleName())){
            lqw.like(BusinessCustomerCorporationMember::getMiddleName ,businessCustomerCorporationMember.getMiddleName());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getLastName())){
            lqw.like(BusinessCustomerCorporationMember::getLastName ,businessCustomerCorporationMember.getLastName());
        }
        if (businessCustomerCorporationMember.getBirthday() != null){
            lqw.eq(BusinessCustomerCorporationMember::getBirthday ,businessCustomerCorporationMember.getBirthday());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getNationality())){
            lqw.eq(BusinessCustomerCorporationMember::getNationality ,businessCustomerCorporationMember.getNationality());
        }
        if (businessCustomerCorporationMember.getDocumentType() != null){
            lqw.eq(BusinessCustomerCorporationMember::getDocumentType ,businessCustomerCorporationMember.getDocumentType());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getDocumentNo())){
            lqw.eq(BusinessCustomerCorporationMember::getDocumentNo ,businessCustomerCorporationMember.getDocumentNo());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getDocumentFrontUrl())){
            lqw.eq(BusinessCustomerCorporationMember::getDocumentFrontUrl ,businessCustomerCorporationMember.getDocumentFrontUrl());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getDocumentBackUrl())){
            lqw.eq(BusinessCustomerCorporationMember::getDocumentBackUrl ,businessCustomerCorporationMember.getDocumentBackUrl());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getDocumentHandyUrl())){
            lqw.eq(BusinessCustomerCorporationMember::getDocumentHandyUrl ,businessCustomerCorporationMember.getDocumentHandyUrl());
        }
        if (businessCustomerCorporationMember.getMemberType() != null){
            lqw.eq(BusinessCustomerCorporationMember::getMemberType ,businessCustomerCorporationMember.getMemberType());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getEmail())){
            lqw.eq(BusinessCustomerCorporationMember::getEmail ,businessCustomerCorporationMember.getEmail());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getCountryCode())){
            lqw.eq(BusinessCustomerCorporationMember::getCountryCode ,businessCustomerCorporationMember.getCountryCode());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getPhoneNumber())){
            lqw.eq(BusinessCustomerCorporationMember::getPhoneNumber ,businessCustomerCorporationMember.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getAddress())){
            lqw.eq(BusinessCustomerCorporationMember::getAddress ,businessCustomerCorporationMember.getAddress());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getCity())){
            lqw.eq(BusinessCustomerCorporationMember::getCity ,businessCustomerCorporationMember.getCity());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getPostCode())){
            lqw.eq(BusinessCustomerCorporationMember::getPostCode ,businessCustomerCorporationMember.getPostCode());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getCountry())){
            lqw.eq(BusinessCustomerCorporationMember::getCountry ,businessCustomerCorporationMember.getCountry());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getKycVeriffUuid())){
            lqw.eq(BusinessCustomerCorporationMember::getKycVeriffUuid ,businessCustomerCorporationMember.getKycVeriffUuid());
        }
        if (businessCustomerCorporationMember.getKycStatus() != null){
            lqw.eq(BusinessCustomerCorporationMember::getKycStatus ,businessCustomerCorporationMember.getKycStatus());
        }
        if (businessCustomerCorporationMember.getCreatedAt() != null){
            lqw.eq(BusinessCustomerCorporationMember::getCreatedAt ,businessCustomerCorporationMember.getCreatedAt());
        }
        if (businessCustomerCorporationMember.getCreatedBy() != null){
            lqw.eq(BusinessCustomerCorporationMember::getCreatedBy ,businessCustomerCorporationMember.getCreatedBy());
        }
        if (businessCustomerCorporationMember.getDeletedAt() != null){
            lqw.eq(BusinessCustomerCorporationMember::getDeletedAt ,businessCustomerCorporationMember.getDeletedAt());
        }
        if (businessCustomerCorporationMember.getDeletedBy() != null){
            lqw.eq(BusinessCustomerCorporationMember::getDeletedBy ,businessCustomerCorporationMember.getDeletedBy());
        }
        if (businessCustomerCorporationMember.getDeleted() != null){
            lqw.eq(BusinessCustomerCorporationMember::getDeleted ,businessCustomerCorporationMember.getDeleted());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getBusinessName())){
            lqw.like(BusinessCustomerCorporationMember::getBusinessName ,businessCustomerCorporationMember.getBusinessName());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporationMember.getBusinessRegistrationNo())){
            lqw.eq(BusinessCustomerCorporationMember::getBusinessRegistrationNo ,businessCustomerCorporationMember.getBusinessRegistrationNo());
        }
        return this.list(lqw);
    }




}
