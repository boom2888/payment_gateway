package com.games.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.games.pay.vo.bindder.LegalEntityVo;
import com.games.pay.vo.bindder.ManagedByVo;
import com.games.pay.vo.bindder.RelationshipVo;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建实体请求 DTO
 * 保持原有字段结构，仅将内部类拆分到 com.games.pay.vo 包下
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEntityDto {

    @NotNull(message = "{relationship.info.not.empty}")
    @Valid
    private RelationshipVo relationship;

    @NotNull(message = "{manager.info.not.empty}")
    @Valid
    private List<ManagedByVo> managedBy;

    @NotNull(message = "{legal.entity.info.not.empty}")
    @Valid
    private LegalEntityVo legalEntity;

}
