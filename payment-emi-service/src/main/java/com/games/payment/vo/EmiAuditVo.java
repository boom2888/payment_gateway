package com.games.payment.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * EMI企业审核对象
 *
 * @author Ticker
 * @date 2025-10-17
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmiAuditVo implements Serializable {

    @NotNull(message = "企业ID不能为空")
    private Long id;

    @NotNull(message = "审核状态不能为空")
    private Integer status;

    /** Binderr OAuth code */
    private String code;

    /** 审核备注 */
    private String remark;
}

