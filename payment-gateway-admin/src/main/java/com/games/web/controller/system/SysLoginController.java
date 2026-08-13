package com.games.web.controller.system;

import com.games.common.constant.Constants;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.entity.SysMenu;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginBody;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.utils.ServletUtils;
import com.games.framework.web.service.SysLoginService;
import com.games.framework.web.service.SysPermissionService;
import com.games.framework.web.service.TokenService;
import com.games.payment.domain.EmiEnterpriseInfo;
import com.games.payment.domain.EmiPersonalInformation;
import com.games.payment.service.IEmiEnterpriseInfoService;
import com.games.payment.service.IEmiPersonalInformationService;
import com.games.system.mapper.SysUserMapper;
import com.games.system.service.ISysMenuService;
import com.games.web.controller.old.BaseCardController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 登录验证
 *
 * @author lor
 */
@Api(tags = "登录")
@RestController
@Slf4j
public class SysLoginController extends BaseCardController
{
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IEmiEnterpriseInfoService emiEnterpriseInfoService;

    @Autowired
    private IEmiPersonalInformationService emiPersonalInformationService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation("登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(), true,  null);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        String status = user.getStatus();
        Long userId = user.getUserId();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        user.setType(sysUser.getType());
        if (status.equals("1")) {
            return AjaxResult.error("商户状态被禁用，请联系管理员");
        }

        // 根据用户ID查询 EMI 表，设置 userType
        try {
            // 先查询企业信息表
            EmiEnterpriseInfo enterpriseInfo = emiEnterpriseInfoService.getByUserId(userId);
            if (enterpriseInfo != null) {
                // 如果在企业表中找到数据，设置 userType 为 2
                user.setUserType(2);
                user.setEnableStatus(enterpriseInfo.getEnabledStatus());
                user.setMaterialStatus(enterpriseInfo.getStatus());
                log.debug("User {} is enterprise type", userId);
            } else {
                // 如果企业表没有，查询个人信息表
                EmiPersonalInformation personalInfo = emiPersonalInformationService.getByUserId(userId);
                if (personalInfo != null) {
                    // 如果在个人表中找到数据，设置 userType 为 1
                    user.setUserType(1);
                    user.setEnableStatus(personalInfo.getEnabledStatus());
                    user.setMaterialStatus(personalInfo.getStatus());
                    log.debug("User {} is personal type", userId);
                } else {
                    // 如果两个表都没有数据，设置为 null
                    user.setUserType(null);
                    user.setEnableStatus(null);
                    user.setMaterialStatus(null);
                    log.debug("User {} has no EMI data", userId);
                }
            }
        } catch (Exception e) {
            log.error("查询 EMI 信息失败: userId={}", userId, e);
            // 查询失败时，userType 设置为 null
            user.setUserType(null);
        }

        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        //如果是商户，则进行权限过滤
        if(isShopRole()){
            BusinessProfileStatus shopStatus = getShopStatus();
            if(!Objects.equals(BusinessProfileStatus.OK.getCode(), shopStatus.getCode())){
                ajax.put("shopStatus", "FAIL");
            }else {
                ajax.put("shopStatus", "PASS");
            }
        }else {
            ajax.put("shopStatus", "PASS");
        }
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation("获取路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
