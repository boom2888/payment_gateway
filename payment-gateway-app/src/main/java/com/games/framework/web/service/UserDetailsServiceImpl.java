package com.games.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author lor
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);



    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return null;
//        User member = gameUserService.getUserByEmailOrGoogle(username);
//        if (StringUtils.isNull(member))
//        {
//            log.info("登录用户：{} 不存在.", username);
////            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
//            throw new BusinessException(MessageUtils.message("user.not.exists"));
//        }
//        SysUser user = new SysUser();
//        user.setUserId(member.getUserId());
////        user.setPhonenumber(member.getMobilePhone());
//        user.setPassword(member.getPassword());
//        user.setUserName(member.getUserEmail());
//        user.setStatus(member.getStatus().toString());
//        user.setSalt(Constants.SALT);
////        user.setAvatar(member.getAvatar());
//        user.setDelFlag(member.getStatus().toString());
//        user.setDeptId(member.getShopId());

//        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
//        {
//            log.info("登录用户：{} 已被删除.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
//        }
//        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
//        {
//            log.info("登录用户：{} 已被停用.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已停用");
//        }
//        return member.toLoginUser();
    }

//    public UserDetails createLoginUser(SysUser user)
//    {
//        return new LoginUser(user, permissionService.getMenuPermission(user));
//    }
}
