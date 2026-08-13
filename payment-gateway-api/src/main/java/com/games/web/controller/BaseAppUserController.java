package com.games.web.controller;

import com.games.common.constant.HttpStatus;
import com.games.common.exception.CustomException;
import com.games.common.utils.MessageUtils;
import com.games.framework.web.service.TokenService;
import com.games.payment.domain.User;
import com.games.payment.domain.Customer;
import com.games.payment.domain.Order;
import com.games.payment.service.ICustomerService;
import com.games.payment.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseAppUserController extends BaseShopApiController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICustomerService customerService;

    public Long getLoginUserId(){
        return getLoginUser().getId();
    }

    public User getLoginUser(){
        Long userId = tokenService.getLoginUserId();
        if (userId == null) {
            throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED);
        }
        User user = userService.getById(userId);
        if (user == null) {
            throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED);
        }
//        if(user.getApiStatus() != 0 || user.getStatus() != UserStatus.OK.ordinal()){
//            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//        }
        return user;
    }

    protected void requireOrderOwner(Order order) {
        if (order == null) {
            throw new CustomException("Order not found", HttpStatus.NOT_FOUND);
        }
        Customer customer = customerService.getById(order.getCustomerId());
        if (customer == null || !getLoginUserId().equals(customer.getUserId())) {
            throw new CustomException("Order access denied", HttpStatus.FORBIDDEN);
        }
    }

}
