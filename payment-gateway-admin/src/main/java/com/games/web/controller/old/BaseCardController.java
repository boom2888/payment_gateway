package com.games.web.controller.old;

import com.games.common.constant.Constants;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.utils.ServletUtils;
import com.games.framework.web.service.TokenService;
import com.games.payment.service.impl.ShopServiceImpl;
import com.games.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseCardController extends BaseController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    ISysRoleService roleService;

    @Autowired
    private ShopServiceImpl shopService;


    public boolean isShopRole(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        System.out.println("后台用户编号:" + loginUser.getUserId());
        if(!SysUser.isAdmin(loginUser.getUserId())){
            List<String> userRoleKeyList = roleService.selectRoleKeyListByUserId(loginUser.getUserId());
            return userRoleKeyList.stream().anyMatch(s -> s.equals(Constants.ROLE_SHOP));
        }
        return false;
    }

    public BusinessProfileStatus getShopStatus(){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer status = shopService.getById(loginUser.getUser().getShopId()).getBusinessProfileStatus();
        return BusinessProfileStatus.getByCode(status);
    }




}
