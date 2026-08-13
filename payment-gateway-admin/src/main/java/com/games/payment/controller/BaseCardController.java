package com.games.payment.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.games.common.constant.Constants;
import com.games.common.constant.HttpStatus;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.enums.UserStatus;
import com.games.common.exception.CustomException;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.ServletUtils;
import com.games.framework.web.service.TokenService;
import com.games.payment.domain.Shop;
import com.games.payment.domain.User;
import com.games.payment.enums.ShopStatus;
import com.games.payment.service.IShopService;
import com.games.payment.service.IUserService;
import com.games.system.domain.WithShopId;
import com.games.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseCardController extends BaseController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    ISysRoleService roleService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private IUserService userService;

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

    public ShopStatus getShopStatus(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String status = shopService.getById(loginUser.getUser().getShopId()).getBusinessProfileStatus()+"";
        return ShopStatus.getByCode(status);
    }

    public void filterShopData(WithShopId withShopId){
        if(isShopRole()){
            ShopStatus shopStatus = getShopStatus();
            if(!StrUtil.equals(shopStatus.getCode(), ShopStatus.OK.getCode())){
                throw new RuntimeException("商户状态:"+shopStatus.getDesc()+",暂无权限查询数据");
            }
            withShopId.setShopId(getSysLoginUser().getUser().getShopId());
        }
    }

    public void mustShopData(WithShopId withShopId){
        if(isShopRole()){
            ShopStatus shopStatus = getShopStatus();
            System.out.println("商户状态:" + shopStatus.getDesc());
            if(!StrUtil.equals(shopStatus.getCode(), ShopStatus.OK.getCode())){
                throw new RuntimeException("商户状态:"+shopStatus.getDesc()+",暂无权限查询数据");
            }
           boolean isMyShopData = withShopId.getShopId().equals(getSysLoginUser().getUser().getShopId());
            if(!isMyShopData){
                throw new RuntimeException("非商户数据,暂无权限查询数据");
            }
        }
    }

    public void mustAdmin(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        boolean isAdmin = loginUser.getUser().getUserId() == 1L;
        if(!isAdmin){
            throw new RuntimeException("非admin用户，无权操作");
        }
    }

    public Shop getShop(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        return shopService.getById(loginUser.getUser().getShopId());
    }
    public User getLoginUser(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        System.out.println("后台用户==>" + JSONUtil.toJsonStr(loginUser));
        Long userId = loginUser.getUserId();
        if (userId == null) {
            throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED);
        }
        User user = userService.getById(userId);
        if (user == null) {
            throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED);
        }
//        if (user.getStatus() == UserStatus.DISABLE.ordinal()) {
//            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//        }
        return user;
    }

    public Long getLoginUserId(){
        return getLoginUser().getId();
    }
}
