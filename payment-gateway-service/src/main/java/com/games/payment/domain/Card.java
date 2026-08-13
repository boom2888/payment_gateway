package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 客户卡令牌对象 customer_card_token
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer_card_token")
public class Card {
    public static Card init(String cardNumber){
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setCreatedAt(new Date());
        return card;
    }

    private static final long serialVersionUID=1L;

    /** ID */
    @TableId(value = "id")
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 收单机构ID */
    @Excel(name = "收单机构ID")
    private Long acquirerId;

    /** 令牌 */
    @Excel(name = "令牌")
    private String token;

    /** 收单机构返回的完整卡号 */
    @Excel(name = "收单机构返回的完整卡号")
    private String cardNumber;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

    /** 卡类型 */
    @Excel(name = "卡类型")
    private String cardType;

    /** 银行卡品牌 */
    @Excel(name = "银行卡品牌")
    private String cardBrand;

    /** 卡到期月份 */
    @Excel(name = "卡到期月份")
    private Long expiryMonth;

    /** 卡到期年份 */
    @Excel(name = "卡到期年份")
    private Long expiryYear;

    /** 4-6位数字卡BIN */
    @Excel(name = "4-6位数字卡BIN")
    private String bin;

    /** 最后一次成功IP */
    @Excel(name = "最后一次成功IP")
    private String scheme;

    /** 卡货币 */
    @Excel(name = "卡货币")
    private String currency;

    /** 发卡银行 */
    @Excel(name = "发卡银行")
    private String issuer;

    /** 发卡国家代码 */
    @Excel(name = "发卡国家代码")
    private String issuerCountry;

    /** 持卡人姓名 */
    @Excel(name = "持卡人姓名")
    private String cardholderName;

    /** 持卡人邮箱地址 */
    @Excel(name = "持卡人邮箱地址")
    private String cardholderEmailAddress;

    @Excel(name = "卡指纹")
    private String cardFinger;

}
