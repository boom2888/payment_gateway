package com.games.web.controller;

import cn.hutool.core.util.StrUtil;
import com.games.api.vo.ApiBaseReq;
import com.games.common.core.controller.BaseController;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.exception.BusinessException;
import com.games.common.utils.ServletUtils;
import com.games.nuvei.NuveiConfig;
import com.games.payment.domain.Shop;
import com.games.payment.service.IShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseShopApiController extends BaseController {

    @Autowired
    protected IShopService shopService;
    @Autowired
    protected NuveiConfig nuveiConfig;

    public Shop getShop(){
        String merId = ServletUtils.getRequest().getParameter("merchantId");
        Shop shop = shopService.getById(merId);
        return shop;
    }

    public Shop getCheckedShop(ApiBaseReq req){
        return getCheckedShop(req, true);
    }

    public Shop getCheckedShop(ApiBaseReq req, boolean check){
        String merId = req.getMerchantId();
//        String merId = ServletUtils.getRequest().getParameter("merchantId");
        Shop shop = shopService.getInfoById(Long.valueOf(merId));
//        req.makeSign(shop.getPubKey());
        if (shop == null){
            throw new BusinessException(StrUtil.format("merchantId {} not exists", merId));
        }
        if(check){
            req.check(shop.getMerchantHashSecretKey(), nuveiConfig.getIsSignDev());
        }
        boolean businessProfileOk = shop.getBusinessProfileStatus() != null &&
                shop.getBusinessProfileStatus().equals(BusinessProfileStatus.OK.getCode());
        if (!businessProfileOk) {
            throw new RuntimeException("商户审核状态未通过，请联系管理处理");
        }

        // 检查商户开关状态
        boolean shopSwitch = shop.getStatus() != null && shop.getStatus() == 0;
        if (!shopSwitch) {
            throw new RuntimeException("渠道授权暂未开启，请联系管理处理");
        }
        return shop;
    }

}
