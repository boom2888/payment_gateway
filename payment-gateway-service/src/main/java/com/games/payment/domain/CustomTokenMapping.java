package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer_token_mapping")
public class CustomTokenMapping {

    /** ID */
    @TableId(value = "id")
    private Long id;

    /** 银行卡号 */
    private String cardNumber;

    /** 用户ID */
    private String userTokenId;

    /** 支付ID: 这个和UserTokenId一一对应，可以不用输入卡信息直接支付 */
    private String paymentOptionId;

    /** 支付次数 */
    private Integer count;

    /** 支付最近一次使用这个记录的orderId */
    private String lastUsedOrderId;

    /** 记录创建时间 */
    private Date createTime;

    /** 支付最近一次使用该记录的时间 */
    private Date lastUsedTime;

    /** 卡对应的用户信息 **/
    private Long customerId;
}
