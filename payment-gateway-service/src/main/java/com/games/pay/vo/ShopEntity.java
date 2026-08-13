package com.games.pay.vo;

import com.games.payment.domain.Shop;
import lombok.Data;

/**
 * 企业客户公司对象 shop
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
public class ShopEntity extends Shop {

    private String feeModelName;

    private String phonenumber;

    private Integer userType;

    private Integer userStatus;

    /** 父商户名称 */
    private String parentBusinessName;


}
