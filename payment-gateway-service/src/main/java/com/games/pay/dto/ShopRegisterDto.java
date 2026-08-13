package com.games.pay.dto;

import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 商户注册请求参数
 *
 * @author Zebord
 * @date 2024-12-04
 */
@Data
@NoArgsConstructor
public class ShopRegisterDto implements Serializable {

private static final long serialVersionUID=1L;

    /** 公司 */
    @NotBlank(message = "{company.name.not.empty}")
    @Length(min = 2, max = 300, message = "公司名称长度必须介于 2 和 300 之间")
    private String businessName;

    /** 手机号 */
    @NotBlank(message = "{phone.number.not.empty}")
    private String mobile;

    /** 账号 */
    @NotBlank(message = "{account.not.empty}")
    @Length(min = 2, max = 300, message = "账号长度必须介于 2 和 300 之间")
    private String userName;

    /** 密码 */
    @NotBlank(message = "{password.not.empty}")
    private String password;

    /** 区分注册渠道1-原来，2-emi */
    public Integer type;

    /** 用户邮箱 */
    private String email;
}
