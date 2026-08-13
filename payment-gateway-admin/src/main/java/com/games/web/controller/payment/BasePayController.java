package com.games.web.controller.payment;

import cn.hutool.json.JSONUtil;
import com.games.common.constant.Constants;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.utils.ServletUtils;
import com.games.framework.web.service.TokenService;
import com.games.payment.domain.Order;
import com.games.payment.domain.RefundOrder;
import com.games.payment.service.IShopService;
import com.games.system.service.ISysRoleService;
import com.games.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class BasePayController extends BaseController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    ISysRoleService roleService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private ISysUserService userService;

    public boolean isShopRole(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        System.out.println("后台用户编号:" + loginUser.getUserId());
        if(!SysUser.isAdmin(loginUser.getUserId())){
            List<String> userRoleKeyList = roleService.selectRoleKeyListByUserId(loginUser.getUserId());
            return userRoleKeyList.stream().anyMatch(s -> s.equals(Constants.ROLE_SHOP));
        }
        return false;
    }

    public LoginUser getSysLoginUser(){
        return tokenService.getLoginUser(ServletUtils.getRequest());
    }

    public Long getShopId(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        System.out.println("后台登录用户:" + JSONUtil.toJsonStr(loginUser));
        return shopService.getById(loginUser.getUser().getShopId()).getId();
    }

    public BusinessProfileStatus getShopStatus(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer status = shopService.getById(loginUser.getUser().getShopId()).getBusinessProfileStatus();
        return BusinessProfileStatus.getByCode(status);
    }

    public void mustShopData(Order order) {
        if (isShopRole()) {
            BusinessProfileStatus shopStatus = getShopStatus();
            System.out.println("商户状态:" + shopStatus.getDesc());
            if (!Objects.equals(shopStatus.getCode(), BusinessProfileStatus.OK.getCode())) {
                throw new RuntimeException("商户状态:" + shopStatus.getDesc() + ",暂无权限查询数据");
            }
            boolean isMyShopData = order.getMerchantId().equals(getSysLoginUser().getUser().getShopId());
            if (!isMyShopData) {
                throw new RuntimeException("非商户数据,暂无权限查询数据");
            }

        }
    }

    public void filterShopData(Order withShopId){
        if(isShopRole()){
            BusinessProfileStatus shopStatus = getShopStatus();
            if(!Objects.equals(shopStatus.getCode(), BusinessProfileStatus.OK.getCode())){
                throw new RuntimeException("商户状态:"+shopStatus.getDesc()+",暂无权限查询数据");
            }
            withShopId.setMerchantId(getSysLoginUser().getUser().getShopId());
        }
    }

    public void mustShopData(RefundOrder refundOrder) {
        if (isShopRole()) {
            BusinessProfileStatus shopStatus = getShopStatus();
            if (!Objects.equals(shopStatus.getCode(), BusinessProfileStatus.OK.getCode())) {
                throw new RuntimeException("商户状态:" + shopStatus.getDesc() + ",暂无权限查询数据");
            }
            boolean isMyShopData = Objects.equals(refundOrder.getMerchantId(), getSysLoginUser().getUser().getShopId());
            if (!isMyShopData) {
                throw new RuntimeException("非商户数据,暂无权限查询数据");
            }
        }
    }

    public void filterShopData(RefundOrder refundOrder){
        if(isShopRole()){
            BusinessProfileStatus shopStatus = getShopStatus();
            if(!Objects.equals(shopStatus.getCode(), BusinessProfileStatus.OK.getCode())){
                throw new RuntimeException("商户状态:"+shopStatus.getDesc()+",暂无权限查询数据");
            }
            refundOrder.setMerchantId(getSysLoginUser().getUser().getShopId());
        }
    }
}
