package com.games.common.utils;

import com.games.common.constant.Constants;
import com.games.common.core.domain.entity.SysRole;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.exception.CustomException;
import com.games.common.constant.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * 安全服务工具类
 * 
 * @author lor
 */
public class SecurityUtils
{
    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new CustomException(MessageUtils.message("security.get.user.account.error"), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        try
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new CustomException(MessageUtils.message("security.get.user.info.error"), HttpStatus.UNAUTHORIZED);
        }
    }

    public static boolean isPoolAdmin(){
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        for (SysRole role : roles){
            boolean isAdmin = Constants.ROLE_ADMIN.equals(role.getRoleId());
            if(isAdmin)return true;
        }
        return false;
    }

    public static Long getShopId(){
        return  SecurityUtils.getLoginUser().getUser().getDeptId();
    }

    public static boolean isShop(){
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        for (SysRole role : roles){
            boolean isAdmin = Constants.ROLE_HEHUOREN.equals(role.getRoleId());
            if(isAdmin)return true;
        }
        return false;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
}
