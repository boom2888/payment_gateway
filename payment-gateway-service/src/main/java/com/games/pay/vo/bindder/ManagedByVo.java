package com.games.pay.vo.bindder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagedByVo {
    @NotNull(message = "{manager.id.not.empty}")
    private Integer id;

    @NotNull(message = "{manager.name.not.empty}")
    private String name;
}
