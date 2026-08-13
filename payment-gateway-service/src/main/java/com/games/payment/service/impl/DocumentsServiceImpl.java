package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.DocumentsMapper;
import com.games.payment.domain.Documents;
import com.games.payment.service.IDocumentsService;

import java.util.List;
import java.util.Map;

/**
 * 文档Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class DocumentsServiceImpl extends ServiceImpl<DocumentsMapper, Documents> implements IDocumentsService {

    /**
     * 查询文档列表
     *
     * @param documents 文档
     * @return 文档
     */
    @Override
    public List<Documents> selectAllList(Documents documents)
    {
        return getBaseMapper().selectAllList(documents);
    }


    @Override
    public List<Documents> queryList(Documents documents) {
        LambdaQueryWrapper<Documents> lqw = Wrappers.lambdaQuery();
        if (documents.getBusinessCustomerCorporationId() != null){
            lqw.eq(Documents::getBusinessCustomerCorporationId ,documents.getBusinessCustomerCorporationId());
        }
        if (documents.getCustomerId() != null){
            lqw.eq(Documents::getCustomerId ,documents.getCustomerId());
        }
        if (StringUtils.isNotBlank(documents.getDocumentUrl())){
            lqw.eq(Documents::getDocumentUrl ,documents.getDocumentUrl());
        }
        if (StringUtils.isNotBlank(documents.getDocumentTypeCode())){
            lqw.eq(Documents::getDocumentTypeCode ,documents.getDocumentTypeCode());
        }
        if (documents.getCreatedAt() != null){
            lqw.eq(Documents::getCreatedAt ,documents.getCreatedAt());
        }
        if (documents.getCreatedBy() != null){
            lqw.eq(Documents::getCreatedBy ,documents.getCreatedBy());
        }
        if (documents.getDeletedAt() != null){
            lqw.eq(Documents::getDeletedAt ,documents.getDeletedAt());
        }
        if (documents.getDeletedBy() != null){
            lqw.eq(Documents::getDeletedBy ,documents.getDeletedBy());
        }
        if (documents.getDeleted() != null){
            lqw.eq(Documents::getDeleted ,documents.getDeleted());
        }
        if (StringUtils.isNotBlank(documents.getOtherDocumentDesc())){
            lqw.eq(Documents::getOtherDocumentDesc ,documents.getOtherDocumentDesc());
        }
        if (documents.getBusinessMemberId() != null){
            lqw.eq(Documents::getBusinessMemberId ,documents.getBusinessMemberId());
        }
        return this.list(lqw);
    }




}
