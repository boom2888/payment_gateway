//package com.games.framework;
//
//import com.games.common.constant.HttpStatus;
//import com.games.common.core.controller.BaseController;
//import com.games.common.enums.UserStatus;
//import com.games.common.exception.CustomException;
//import com.games.common.utils.MessageUtils;
//import com.games.credit.card.domain.User;
//import com.games.credit.card.service.IUserService;
//import com.games.framework.web.service.TokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public abstract class BaseAppUserController extends BaseController {
//    @Autowired
//    private TokenService tokenService;
//    @Autowired
//    private IUserService userService;
//
//    public Long getLoginUserId(){
//        return getLoginUser().getUserId();
//    }
//
//    public User getLoginUser(){
//        Long userId = tokenService.getLoginUserId();
//        if (userId == null) {
//            throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED);
//        }
//        User user = userService.getById(userId);
//        if (user == null) {
//            throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED);
//        }
//        if (user.getStatus() == UserStatus.DISABLE.ordinal()) {
//            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//        }
//        return user;
//    }
//
//}
