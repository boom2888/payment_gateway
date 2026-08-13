package com.games.system.domain;

import com.games.common.annotation.Excel;
import com.games.common.constant.Constants;

public class WithShopId extends BaseShop{
    /** 商户 */
    @Excel(name = "商户", dictType = "SHOP_ID")
    private Long shopId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /** 是否是平台商户 */
    public boolean isPlantShop(){
        return shopId != null && shopId.equals(Constants.PLANT_SHOP_ID);
    }
}
