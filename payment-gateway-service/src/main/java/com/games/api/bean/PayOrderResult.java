package com.games.api.bean;

import com.games.api.dto.TdsDto;
import com.games.api.enums.OrderStatus;
import com.games.payment.domain.Card;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderResult {
    // 是否成功
    private boolean isSucc;

    public PayOrderResult(boolean isSucc, TdsDto tdsDto) {
        this.isSucc = isSucc;
        this.tdsDto = tdsDto;
        this.isTds = tdsDto != null;
    }

    public PayOrderResult(String message) {
        this.isSucc = false;
        this.message = message;
        this.isTds = false;
    }

    public PayOrderResult(boolean isSucc, TdsDto tdsDto, OrderStatus status) {
        this.isSucc = isSucc;
        this.tdsDto = tdsDto;
        this.orderStatus = status;
        this.isTds = tdsDto != null;
    }

    @ApiModelProperty(value = "Whether 3DS modal is required", example = "true", required = true)
    private boolean isTds;
    private TdsDto tdsDto;
    private OrderStatus orderStatus;
    // 上游渠道系统单号
    private String superOrderId;
    // 错误信息
    private String message;

    private String acquirerId;
    private String userPaymentOptionId;
    // 当前交易流水
    private String transactionId;
    // 关联交易流水
    private String relatedTransactionId;

    private Boolean is3DSAuthenticated;

    private Card cardInfo;

    /**
     * Token（用于操作代码[23] Create Token with Sale）
     */
    private String token;
}
