package com.games.pay.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.games.common.constant.Constants;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.entity.SysRole;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.utils.MessageUtils;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.utils.DateUtils;
import com.games.common.utils.SecurityUtils;
import com.games.pay.dto.ShopRegisterDto;
import com.games.pay.enums.ShopType;
import com.games.payment.domain.Shop;
import com.games.payment.service.impl.ShopServiceImpl;
import com.games.system.service.ISysRoleService;
import com.games.system.service.ISysUserService;
import com.games.system.service.impl.SysUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AppUserService {

    private final ShopServiceImpl shopService;
    private final ISysUserService sysUserService;
    private final ISysRoleService roleService;
    private final SysUserServiceImpl sysUserServicei;

    /**
     * 商户注册
     */
    @Transactional
    public AjaxResult registerShopUser(ShopRegisterDto dto) {
        // 同步新增商户角色的后台用户
        SysUser user = new SysUser();
        user.setUserName(dto.getUserName());
        user.setDeptId(1L);

        // 帐号状态设置为禁用
        user.setPhonenumber(dto.getMobile());
        user.setNickName(dto.getBusinessName());
        user.setCreateBy(dto.getBusinessName());
        user.setEmail(dto.getEmail());
        user.setType(dto.getType());
        // user.setStatus("1");
        user.setPassword(SecurityUtils.encryptPassword(dto.getPassword()));
        AjaxResult result = sysUserServicei.checkRegisterUser(user);
        if (result != null) {
            return result;
        }

        sysUserService.insertUser(user);
        String roleKey = Constants.ROLE_SHOP;
        if (ShopType.EMI_ROLE.getCode().equals(dto.getType())) {
            roleKey = Constants.EMI_ROLE_SHOP;
        }
        SysRole role = roleService.selectRoleByRoleKey(roleKey);
        if (ObjectUtil.isEmpty(role)) {
            return AjaxResult.error(MessageUtils.message("merchant.role.not.exist"));
        }
        // 分配商户角色
        user.setRoleIds(new Long[] { role.getRoleId() });
        // 新增用户岗位关联
        sysUserService.insertUserPost(user);
        // 新增用户与角色管理
        sysUserService.insertUserRole(user);

        // 新增商户
        Shop shop = new Shop();
        BeanUtils.copyProperties(dto, shop);
        shop.setBusinessProfileStatus(BusinessProfileStatus.INITIAL.getCode());
        shop.setMerchantHashSecretKey(RandomUtil.randomString(32));
        shop.setUserId(user.getUserId());
        shop.setRegistrationDate(DateUtils.parseDate(DateUtils.getDate()));
        shop.setSaasUserCorporationId(1L);
        // 设置渠道商户标识，格式：QBSG*BusinessName
        shop.setDescriptor(null);
        shopService.save(shop);

        // 关联商户
        user.setShopId(shop.getId());
        sysUserService.updateUser(user);

        return AjaxResult.success();
    }

}
