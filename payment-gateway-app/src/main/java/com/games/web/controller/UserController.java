//package com.games.web.controller;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.games.common.config.RuoYiConfig;
//import com.games.common.constant.HttpStatus;
//import com.games.common.core.domain.AjaxResult;
//import com.games.common.core.domain.R;
//import com.games.common.core.domain.model.LoginUser;
//import com.games.common.enums.UserStatus;
//import com.games.common.enums.YesNo;
//import com.games.common.exception.BaseException;
//import com.games.common.exception.BusinessException;
//import com.games.common.exception.CustomException;
//import com.games.common.utils.MessageUtils;
//import com.games.common.utils.SecurityUtils;
//import com.games.common.utils.ServletUtils;
//import com.games.common.utils.file.FileUploadUtils;
//import com.games.credit.InviterService;
//import com.games.credit.WalletService;
//import com.games.credit.card.domain.Account;
//import com.games.credit.card.domain.Card;
//import com.games.credit.card.dto.ShopRegisterDto;
//import com.games.credit.card.service.IAccountService;
//import com.games.credit.card.service.ICardService;
//import com.games.credit.card.service.IUserService;
//import com.games.credit.card.vo.InviteEarnedVo;
//import com.games.credit.card.vo.InviteUserVo;
//import com.games.credit.card.vo.OpenAccountCache;
//import com.games.credit.card.vo.PageReq;
//import com.games.framework.web.service.SysLoginService;
//import com.games.framework.web.service.TokenService;
//import com.games.logic.BooleanType;
//import com.games.logic.card.enums.project.FromSource;
//import com.games.logic.card.enums.user.AuthLevel;
//import com.games.logic.emums.MsgCodeType;
//import com.games.logic.service.*;
//import com.games.logic.vo.*;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.Assert;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import com.games.credit.card.domain.User;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * 登录验证
// *
// * @author lor
// */
//@Api("用户管理")
//@RestController
//@Slf4j
//public class UserController
//{
//    @Autowired
//    private SysLoginService loginService;
//    @Autowired
//    private TokenService tokenService;
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private AppUserService appUserService;
//    @Autowired
//    private CheckCodeService checkCodeService;
//    @Autowired
//    private WalletService walletService;
//    @Autowired
//    private InviterService inviterService;
//
//    @ApiOperation("邮箱注册")
//    @GetMapping("/common/emailRegist")
//    @ApiResponse(code = 200, message = "Token信息", response = String.class)
//    public R regist(RegistVo registVo)
//    {
//        User user = appUserService.regist(registVo, BooleanType.NO.code());
//        walletService.autoGenAddress(user.getUserId());
//        String token = loginService.login(registVo.getEmail(), registVo.getPassword(), null, null, false, null);
//        return R.okData(token);
//    }
//
////    @ApiOperation("商户注册")
////    @PostMapping("registerShopUser")
////    public AjaxResult registerShopUser(@RequestBody @Validated ShopRegisterDto dto)
////    {
////        return appUserService.registerShopUser(dto);
////    }
//
////    @ApiOperation("取消注册")
////    @GetMapping("/unRegist")
////    public R unRegist(){
////        LoginUser loginUser = tokenService.getAppLoginUser(ServletUtils.getRequest());
////        User user = userService.getById(loginUser.getUserId());
////        if(user == null){ throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED); }
////        appUserService.unRegist(user.getUserId());
////        return R.ok();
////    }
//
//    @ApiOperation("邮箱登陆")
//    @GetMapping("/common/emailLogin")
//    @ApiResponse(code = 200, message = "Token信息", response = String.class)
//    public R login(LoginVo loginVo)
//    {
//        Checker.checkUserPass(loginVo.getPassword());
//        String token = loginService.login(loginVo.getUsername(), loginVo.getPassword(), null, null, false, null);
//        User user = appUserService.getUserByEmailOrGoogle(loginVo.getUsername());
//        if(user.getStatus() == UserStatus.DISABLE.ordinal()){
//            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//        }
//        if(user.getInviteId() == null){
//            if(StrUtil.isBlank(loginVo.getInviteCode())){
//                throw new CustomException(MessageUtils.message("USER_INVITE_CODE_ERR"), HttpStatus.UNAUTHORIZED);
//            }
//            User inviter = userService.getByInviteCode(loginVo.getInviteCode());
//            if(inviter == null){
//                throw new CustomException(MessageUtils.message("USER_INVITE_CODE_ERR"), HttpStatus.UNAUTHORIZED);
//            }
//            if (inviterService.hasCycle(user.getUserId(), inviter.getUserId())) {
//                throw new CustomException(MessageUtils.message("USER_INVITE_CODE_ERR"), HttpStatus.UNAUTHORIZED);
//            }
//            user.setInviteId(inviter.getUserId());
//            user.setUpdateTime(new Date());
//            userService.updateById(user);
//        }
//        return R.okData(token);
//    }
//
////    @ApiOperation("验证码登陆")
////    @GetMapping("/common/codeLogin")
////    @ApiResponse(code = 200, message = "Token信息", response = String.class)
////    public R loginByCode(LoginCodeVo loginVo)
////    {
////        checkCodeService.verifyNsgCode(MsgCodeType.CODE_LOGIN, loginVo.getUsername(), loginVo.getCode());
////        User user = appUserService.getUserByEmailOrGoogle(loginVo.getUsername());
////        if(user.getStatus() == UserStatus.DISABLE.ordinal()){
////            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
////        }
////        String token = loginService.login(loginVo.getUsername(), null, null, null, false, user.toLoginUser());
////        return R.okData(token);
////    }
//
//    @ApiOperation("获取用户信息")
//    @GetMapping("getInfo")
//    @ApiResponse(code = 200, message = "用户信息", response = GameUserVo.class)
//    public R<GameUserVo> getInfo()
//    {
//        GameUserVo userVo = appUserService.getUserInfo(tokenService.getLoginUserId());
//        userVo.totalGift();
//        return R.okData(userVo);
//    }
//
//    @ApiOperation("我的邀请明细")
//    @GetMapping("/myInviteList")
//    @ApiResponse(code = 200, message = "返佣明细", response = InviteEarnedVo.class)
//    public R<List<InviteUserVo>> myInviteList(PageReq req) {
//        PageHelper.startPage(req.getPageNum(), req.getPageSize());
//        List<User> invitedList = userService.getUserInviteList(tokenService.getLoginUserId());
//        long total = new PageInfo(invitedList).getTotal();
//        List<InviteUserVo> tradeVoList = BeanUtil.copyToList(invitedList, InviteUserVo.class);
//        return R.okPage(tradeVoList, total);
//    }
//
//    @ApiOperation("验证邀请码")
//    @GetMapping("/common/verifyInviteCode")
//    public R verifyInviteCode(String code)
//    {
//       User user = userService.getByInviteCode(code);
//       boolean isSucc = user != null;
//       return R.okData(isSucc);
//    }
//
//
//    @ApiOperation("修改用户名")
//    @GetMapping("editUserName")
//    @ApiResponse(code = 200, message = "用户信息", response = GameUserVo.class)
//    public AjaxResult editUserName(String userName)
//    {
//        if(StrUtil.isEmpty(userName))throw new BusinessException(MessageUtils.message("USER_NAME_NULL_ERR"));
////        if(!appUserService.isUserNameEnable(userName))throw new BusinessException(MessageUtils.message("USER_NAME_NO_ENABLE_ERR"));
//        LoginUser loginUser = tokenService.getAppLoginUser(ServletUtils.getRequest());
//        User user = userService.getById(loginUser.getUserId());
//        if(user == null){ throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED); }
//        if(user.getStatus() == UserStatus.DISABLE.ordinal()){
//            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//        }
//        user.setUserName(userName);
//        userService.updateById(user);
//        GameUserVo gameUserVo = BeanUtil.copyProperties(user, GameUserVo.class);
//        gameUserVo.change(user);
//        return AjaxResult.successData(gameUserVo);
//    }
//
//    /**
//     * 修改头像
//     */
//    @ApiOperation("修改头像")
//    @PostMapping("/editAvatar")
//    public AjaxResult editAvatar(MultipartFile file)
//    {
//        if(file == null)throw new BusinessException(MessageUtils.message("USER_AVATAR_NULL_ERR"));
//        try
//        {
//            // 上传文件路径
//            String filePath = RuoYiConfig.getUploadPath();
//            // 上传并返回新文件名称
//            String fileName = new FileUploadUtils().upload(filePath, file).realPath;
//            String url = RuoYiConfig.getFilehost() + fileName;
//
//            LoginUser loginUser = tokenService.getAppLoginUser(ServletUtils.getRequest());
//            User user = userService.getById(loginUser.getUserId());
//            if(user == null){ throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED); }
//            if(user.getStatus() == UserStatus.DISABLE.ordinal()){
//                throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//            }
//            user.setAvatar(url);
//            userService.updateById(user);
//
//            GameUserVo gameUserVo = BeanUtil.copyProperties(user, GameUserVo.class);
//            gameUserVo.change(user);
//            return AjaxResult.successData(gameUserVo);
//        }
//        catch (Exception e)
//        {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    @ApiOperation("修改密码(如果用户未设置过密码则不校验旧密码，比如谷歌三方账号, 是否设置密码看用户信息的password字段)")
//    @GetMapping("editUserPass")
//    @ApiResponse(code = 200, message = "用户信息", response = GameUserVo.class)
//    public AjaxResult editUserPass(String oldPass, String newPass)
//    {
//        Checker.checkUserPass(newPass);
//        LoginUser loginUser = tokenService.getAppLoginUser(ServletUtils.getRequest());
//        User user = userService.getById(loginUser.getUserId());
//        if(user == null){ throw new CustomException(MessageUtils.message("USER_TOKEN_NULL_LOGIC"), HttpStatus.UNAUTHORIZED); }
//        if(user.getStatus() == UserStatus.DISABLE.ordinal()){
//            throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//        }
//        String password = user.getPassword();
//
//        if(StrUtil.isNotEmpty(user.getPassword())){
//            if(StrUtil.isEmpty(oldPass))throw new BusinessException(MessageUtils.message("USER_OLD_PASS_NULL_ERR"));
//            if (!SecurityUtils.matchesPassword(oldPass, password))
//            {
//                throw new BusinessException(MessageUtils.message("USER_OLD_PASS_NOMUCH_ERR"));
//            }
//            if (SecurityUtils.matchesPassword(newPass, password))
//            {
//                throw new BusinessException(MessageUtils.message("NEW_OLD_PASS_EQ_ERR"));
//            }
//        }
//        user.setPassword(SecurityUtils.encryptPassword(newPass));
//        userService.updateById(user);
//        GameUserVo gameUserVo = BeanUtil.copyProperties(user, GameUserVo.class);
//        gameUserVo.change(user);
//        return AjaxResult.successData(gameUserVo);
//    }
//
//    @ApiOperation("发送邮箱验证码")
//    @GetMapping("/common/sendAuthEmailCode")
//    public R sendCode(SendEmailVo vo)
//    {
//        Checker.checkEmailAddress(vo.getEmail());
//        if(vo.getType() == null)throw new BusinessException(MessageUtils.message("EMAIL_CODE_TYPE_ERR"));
//        if(vo.getType() == MsgCodeType.REG.ordinal()){
//            User oldMember = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//            Assert.isNull(oldMember, MessageUtils.message("EMAIL_HAS_REGED_ERR"));
//        }
//        if(vo.getType() == MsgCodeType.BIND_EMAIL.ordinal()){
//            User oldMember = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//            Assert.isNull(oldMember, MessageUtils.message("EMAIL_HAS_BIND_ERR"));
//        }
//        if(vo.getType() == MsgCodeType.BIND_2FA.ordinal()){
//            User oldMember = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//            if(oldMember == null) throw new BaseException(MessageUtils.message("user.not.exists"));
//            if(StrUtil.equals(YesNo.YES.name(), oldMember.getGoogleStatus()))throw new BusinessException(MessageUtils.message("HAS_BIND_2FA_ERR"));
//        }
//        if(vo.getType() == MsgCodeType.CODE_LOGIN.ordinal()){
//            User oldMember = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//            if(oldMember == null) throw new BaseException(MessageUtils.message("user.not.exists"));
////            if(!StrUtil.equals(YesNo.YES.name(), oldMember.getGoogleStatus()))throw new BusinessException("NOT_BIND_2FA_ERR");
//        }
//        if(vo.getType() == MsgCodeType.RESET_PASS.ordinal()){
//            User oldMember = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//            if(oldMember == null) throw new BaseException(MessageUtils.message("user.not.exists"));
//        }
//        checkCodeService.sendEmailCode(vo);
//        return R.ok();
//    }
//
//
////    @ApiOperation("绑定邮箱Or修改邮箱")
//    @GetMapping("/bindEmail")
//    public R bindEmail(BindEmailVo vo)
//    {
//        LoginUser loginUser = tokenService.getAppLoginUser(ServletUtils.getRequest());
//        appUserService.bindEmail(loginUser.getUserId(), vo);
//        return R.ok();
//    }
//
//    @ApiOperation("重置登录2FA、密码S1-验证邮箱验证码")
//    @GetMapping("/common/checkEmailCode")
//    public R checkEmailCode(ResetPassS1Vo vo)
//    {
//        if(vo.getType() == MsgCodeType.CODE_LOGIN.ordinal() || vo.getType() == MsgCodeType.BIND_2FA.ordinal() || vo.getType() == MsgCodeType.RESET_PASS.ordinal()){
//            User user = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//            if(user == null) throw new BaseException(MessageUtils.message("user.not.exists"));
//            if(user.getStatus() == UserStatus.DISABLE.ordinal()){
//                throw new CustomException(MessageUtils.message("USER_DISABLE_ERR"), HttpStatus.UNAUTHORIZED);
//            }
//        }
//        MsgCodeType codeType = MsgCodeType.values()[vo.getType()];
//        checkCodeService.verifyNsgCode(codeType, vo.getEmail(), vo.getCode(), false);
//        return R.ok();
//    }
//
//    @ApiOperation("重置登录密码S2-修改密码")
//    @GetMapping("/common/resetPassword")
//    public R resetPassword(ResetPassVo vo)
//    {
//        Checker.checkUserPass(vo.getPassword());
//        User user = appUserService.getUserByEmailOrGoogle(vo.getEmail());
//        if(user == null) throw new BaseException(MessageUtils.message("user.not.exists"));
//        appUserService.resetPassword(user, vo);
//        return R.ok();
//    }
//}
