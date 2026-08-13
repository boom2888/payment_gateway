package com.games.shift4.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Sale/Authorisation/Card-Only Validation 请求参数（[1]/[2]/[6]）
 */
@Data
public class Shift4ReqVo {
    /**
     * Shift4 分配的网关商户ID。
     * Type: [A-Z0-9_], Length: 3-8, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "商户ID不能为空")
    @Size(min = 3, max = 8, message = "商户ID长度必须在3到8位之间")
    private String M;

    /**
     * 请求认证密钥，详见 Appendix A Message Cipher。
     * Type: [0-9A-Za-z], Length: 1-32, Stand-Alone Operations: [1][2][6], Required: m.
     */
    private String K;

    /**
     * 操作代码，用于确定请求服务（Basic Operations 列表，1 表示 Sale）。
     * Type: [0-9], Length: 1-3, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "操作代码不能为空")
    @Size(min = 1, max = 3, message = "操作代码长度必须在1到3位之间")
    private String O;

    /**
     * 请求ID，需在每个MID下唯一，可用于对账，不得包含明文持卡人数据。
     * Type: [A-Za-z0-9-], Length: 1-32, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "请求ID不能为空")
    @Size(min = 1, max = 32, message = "请求ID长度必须在1到32位之间")
    private String a1;

    /**
     * 支付来源类型；有效值：2 Online、3 Telephone、4 Mail、5 Virtual Terminal。
     * Type: [0-9], Length: 1-2, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 2)
    private String a2;

    /**
     * 账单金额（无小数点，按币种指数格式化，最小 0.01 EUR 或等值）。
     * Type: [0-9], Length: 1-12, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "账单金额不能为空")
    @Size(min = 1, max = 12, message = "账单金额长度必须在1到12位之间")
    private String a4;

    /**
     * 交易币种（ISO 4217 alpha-3，需预先在平台配置）。
     * Type: [A-Z], Length: 3-3, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "交易币种不能为空")
    @Size(min = 3, max = 3, message = "交易币种长度必须为3位")
    private String a5;

    /**
     * 交易日期（本地日期，yyMMdd）。
     * Type: numeric, Length: 6-6, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 6, max = 6)
    private String a6;

    /**
     * 交易时间（本地时间，HHmmss）。
     * Type: numeric, Length: 6-6, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 6, max = 6)
    private String a7;

    /**
     * 交易类型；1 首次代扣、2 后续代扣、5 Card-Only Validation、6 Straight、8/9/10/11/12 COF/订阅等。
     * Type: [0-9], Length: 1-2, Stand-Alone Operations: [1][2][6], Required: c（[1]/[2] 为循环交易时必填）。
     */
    @Size(min = 1, max = 2)
    private String a9;

    /**
     * 授权类型：1 Final（默认）、2 Pre、3 Deferred；引用预授权时需包含 a4。
     * Type: [1-3], Length: 1-1, Stand-Alone Operations: [1][2][6], Required: c（[1]/[2] 必填）。
     */
    @Size(min = 1, max = 1)
    private String a10;

    /**
     * 多次捕获标记，期望捕获次数；仅支持无卡环境，默认1，范围2-98。
     * Type: [0-9], Length: 1-2, Stand-Alone Operations: [1][2][6], Required: c（[2] 必填）。
     */
    @Size(min = 1, max = 2)
    private String a11;

    /**
     * 部分授权标记：0 仅全额（默认），1 接受部分授权。
     * Type: [0,1], Length: 1-1, Stand-Alone Operations: [1][2][6], Required: c（[2] 必填）。
     */
    @Size(min = 1, max = 1)
    private String a14;

    /**
     * 循环扣款频率；取值 1-12 对应日/周/月/年度等，12 表示非计划（默认）。
     * Type: [0-9], Length: 1-2, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 2)
    private String a19;

    /**
     * PAN 主账号。
     * Type: [0-9], Length: 8-19, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "PAN主账号不能为空")
    @Size(min = 8, max = 19, message = "PAN主账号长度必须在8到19位之间")
    private String b1;

    /**
     * 卡有效期月份（mm）。
     * Type: [0-9], Length: 2-2, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "卡有效期月份不能为空")
    @Size(min = 2, max = 2, message = "卡有效期月份长度必须为2位")
    private String b3;

    /**
     * 卡有效期年份（yy）。
     * Type: [0-9], Length: 2-2, Stand-Alone Operations: [1][2][6], Required: m.
     */
    @NotBlank(message = "卡有效期年份不能为空")
    @Size(min = 2, max = 2, message = "卡有效期年份长度必须为2位")
    private String b4;

    /**
     * 安全码 CVV/CVC。
     * Type: [0-9], Length: 3-4, Stand-Alone Operations: [1][2][6], Required: m（[6] 可选）。
     */
    @NotBlank(message = "安全码不能为空")
    @Size(min = 3, max = 4, message = "安全码长度必须在3到4位之间")
    private String b5;

    /**
     * Passthrough 钱包标识：applepay、googlepay、samsungpay、vts_mdes_token。
     * Type: [A-Za-z\\ ], Length: 8-14, Stand-Alone Operations: [1][2][6], Required: c（如来自上述钱包则必填）。
     */
    @Size(min = 8, max = 14)
    private String b21;

    /**
     * 持卡人全名，最少5字符；长度不足需补非空字符或不传。
     * Type: [a-zA-Z ], Length: 5-45, Stand-Alone Operations: [1][2][6], Required: c，3DS 推荐，Visa 3DS 必填。
     */
    @Size(min = 5, max = 45)
    private String c1;

    /**
     * 持卡人联系电话；Visa 3DS 需电话或邮箱至少一项。
     * Type: [0-9\\-\\. ], Length: 5-32, Stand-Alone Operations: [1][2][6], Required: c（Visa 3DS，若未传 c3）。
     */
    @Size(min = 5, max = 32)
    private String c2;

    /**
     * 持卡人邮箱（有效邮件地址）；卡到店默认传递；Visa 3DS 需电话或邮箱至少一项。
     * Type: email, Length: 7-64, Stand-Alone Operations: [1][2][6], Required: c（3DS 发起或未传 c2 时）。
     */
    @Size(min = 7, max = 64)
    private String c3;

    /**
     * 账单地址门牌号；启用 AVS 时传入，传入后 c5 不含门牌号。
     * Type: [0-9], Length: 1-16, Stand-Alone Operations: [1][2][6], Required: o（3DS 推荐）。
     */
    @Size(min = 1, max = 16)
    private String c4;

    /**
     * 账单地址街道名（不含门牌号）。
     * Type: [a-zA-Z0-9 \\-], Length: 4-50, Stand-Alone Operations: [1][2][6], Required: o（3DS 推荐）。
     */
    @Size(min = 4, max = 50)
    private String c5;

    /**
     * 账单地址城市。
     * Type: [a-zA-Z \\-], Length: 3-30, Stand-Alone Operations: [1][2][6], Required: o（3DS 推荐）。
     */
    @Size(min = 3, max = 30)
    private String c7;

    /**
     * 账单地址州/省代码（ISO 3166-2 二级代码）。
     * Type: [a-zA-Z0-9], Length: 1-3, Stand-Alone Operations: [1][2][6], Required: o（3DS 推荐）。
     */
    @Size(min = 1, max = 3)
    private String c8;

    /**
     * 账单地址国家代码（ISO 3166-1 alpha-2）。
     * Type: [A-Z], Length: 2-2, Stand-Alone Operations: [1][2][6], Required: o（3DS 推荐）。
     */
    @Size(min = 2, max = 2)
    private String c9;

    /**
     * 账单地址邮编/ZIP，支持 AVS 检查。
     * Type: [a-zA-Z0-9 \\-], Length: 1-9, Stand-Alone Operations: [1][2][6], Required: c（3DS 推荐）。
     */
    @Size(min = 1, max = 9)
    private String c10;

    /**
     * 持卡人名。
     * Type: [A-Za-z], Length: 1-50, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 50)
    private String c22;

    /**
     * 持卡人姓。
     * Type: [A-Za-z], Length: 1-50, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 50)
    private String c23;

    /**
     * 持卡人IP；卡到店场景建议发送；Visa 3DS 必填。
     * Type: [0-9\\.], Length: 7-15, Stand-Alone Operations: [1][2][6], Required: c（Visa 3DS 为 m）。
     */
    @Size(min = 7, max = 15)
    private String d1;

    /**
     * Echo 参数，任意值会原样返回；勿包含明文持卡人数据。
     * Type: [a-zA-Z0-9], Length: 3-128, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 3, max = 128)
    private String d2;

    /**
     * 是否跳过风控检查：0 送风控（默认），1 不送；仅 Smart Guard 商户可用。
     * Type: [0-1], Length: 1-1, Stand-Alone Operations: [1][2][6], Required: o（[6] 不适用）。
     */
    @Size(min = 1, max = 1)
    private String f21;

    /**
     * 自定义风控阈值，0-1000；仅 Smart Guard Plus 商户可用。
     * Type: [0-9], Length: 0-4, Stand-Alone Operations: [1][2][6], Required: o（[6] 不适用）。
     */
    @Size(max = 4)
    private String f22;

    /**
     * 二级商户ID（Payment Facilitator 场景）。
     * Type: [0-9], Length: 1-15, Stand-Alone Operations: [1][2][6], Required: c（支付服务商）。
     */
    @Size(min = 1, max = 15)
    private String h3;

    /**
     * 二级商户电话。
     * Type: [0-9\\-\\.], Length: 5-32, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 5, max = 32)
    private String h8;

    /**
     * 商户参考号，作为辅助交易参考号；勿包含明文持卡人数据。
     * Type: Text / AMEX: [a-zA-Z0-9], Length: 1-32（AMEX 1-30）, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 32)
    private String h9;

    /**
     * 卖家信息，需包含 Seller ID，字段以“|”分隔：Seller ID/country/city/street/postal code/state。
     * Type: [a-zA-Z0-9\\-\\ |], Length: 9-164, Stand-Alone Operations: [1][2][6], Required: c（仅 [1]）。
     */
    @Size(min = 9, max = 164)
    private String h15;

    /**
     * 交易自由文本描述。
     * Type: Text, Length: 5-64, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 5, max = 64)
    private String i1;

    /**
     * 动态账单描述，格式“DBA*City”，DBA 最多22字符且不含“*”。
     * Type: Text, Length: 1-39, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 39)
    private String i2;

    /**
     * 3D Secure 数据，格式 ECI:CAVV/AAV:XID；第三方3DS 结果传入。
     * Type: [a-zA-Z0-9:\\=+], Length: 10-128, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 10, max = 128)
    private String i8;

    /**
     * 3D Secure 协议版本，可选 1.0/2.0/2.1.0/2.2.0。
     * Type: [0-9], Length: 3-5, Stand-Alone Operations: [1][2][6], Required: c（使用 i8 时必填）。
     */
    @JsonProperty("3ds_version")
    @Size(min = 3, max = 5)
    private String threeDsVersion;

    /**
     * 3DS Directory Server 交易ID；3ds_version>=2.0 且使用 i8 时必填。
     * Type: [0-9A-Za-z,-], Length: 36, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @JsonProperty("3ds_dstrxid")
    @Size(min = 36, max = 36)
    private String threeDsDstrxid;

    /**
     * 3DS 渠道（browser/app 等），示例：03。
     */
    @JsonProperty("3ds_channel")
    private String threeDsChannel;

    /**
     * 账户收款人生日（YYYYMMDD）。
     * Type: date, Length: 8-8, Stand-Alone Operations: [1][2][6], Required: c（6012 英国商户必填）。
     */
    @Size(min = 8, max = 8)
    private String j1;

    /**
     * 收款人 PAN 或账号（6012 英国商户长度4-10）。
     * Type: [a-zA-Z0-9*], Length: 4-34, Stand-Alone Operations: [1][2][6], Required: c（AFT/6012 UK 必填）。
     */
    @Size(min = 4, max = 34)
    private String j2;

    /**
     * 收款人邮编。
     * Type: [a-zA-Z0-9 /-/], Length: 2-6, Stand-Alone Operations: [1][2][6], Required: c（6012 UK 必填）。
     */
    @Size(min = 2, max = 6)
    private String j3;

    /**
     * 收款人部分姓氏。
     * Type: [a-zA-Z*], Length: 2-6, Stand-Alone Operations: [1][2][6], Required: c（6012 UK 必填）。
     */
    @Size(min = 2, max = 6)
    private String j4;

    /**
     * 收款人名。
     * Type: [a-zA-Z0-9 \\-], Length: 1-30, Stand-Alone Operations: [1][2][6], Required: c（AFT 必填）。
     */
    @Size(min = 1, max = 30)
    private String j5;

    /**
     * 收款人街道地址；收款人=付款人时 AFT 必填，CA/US/COL/NIC 场景必填。
     * Type: ['\"0-9A-Za-z], Length: 1-50, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 50)
    private String j6;

    /**
     * 收款人城市；收款人=付款人时 AFT 必填，CA/US/COL/NIC 场景必填。
     * Type: ['\"0-9A-Za-z], Length: 1-25, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 25)
    private String j7;

    /**
     * 收款人州/省代码；AFT 且 CA/US/COL/NIC 场景必填。
     * Type: [0-9A-Za-z], Length: 2-3, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 2, max = 3)
    private String j8;

    /**
     * 收款人国家代码。
     * Type: [A-Z], Length: 3-3, Stand-Alone Operations: [1][2][6], Required: c（AFT 必填）。
     */
    @Size(min = 3, max = 3)
    private String j9;

    /**
     * 收款人电话。
     * Type: [0-9-], Length: 1-20, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 20)
    private String j10;

    /**
     * 收款人生日（MMDDYYYY）。
     * Type: [0-9], Length: 8-8, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 8, max = 8)
    private String j11;

    /**
     * 收款人邮编。
     * Type: [a-zA-Z0-9 /-/], Length: 1-10, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 10)
    private String j12;

    /**
     * 收款人姓氏。
     * Type: [a-zA-Z0-9 \\-], Length: 1-30, Stand-Alone Operations: [1][2][6], Required: c（AFT 必填）。
     */
    @Size(min = 1, max = 30)
    private String j13;

    /**
     * 付款人名；当付款人≠收款人且为 AFT（[1]/[2]）时必填。
     * Type: [a-zA-Z0-9], Length: 1-30, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 30)
    private String s10;

    /**
     * 付款人姓；当付款人≠收款人且为 AFT（[1]/[2]）时必填。
     * Type: [a-zA-Z0-9], Length: 1-25, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 25)
    private String s11;

    /**
     * 付款人街道地址；当付款人≠收款人且为 AFT（[1]/[2]）时必填。
     * Type: [a-zA-Z0-9], Length: 1-50, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 50)
    private String s12;

    /**
     * 付款人城市；当付款人≠收款人且为 AFT（[1]/[2]）时必填。
     * Type: [a-zA-Z0-9], Length: 1-25, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 25)
    private String s13;

    /**
     * 付款人州代码；AFT 且美国/加拿大场景必填。
     * Type: [a-zA-Z0-9], Length: 2-30, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 2, max = 30)
    private String s14;

    /**
     * 付款人国家代码（ISO 三字码）；当付款人≠收款人且为 AFT（[1]/[2]）时必填。
     * Type: [A-Z], Length: 3-3, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 3, max = 3)
    private String s15;

    /**
     * 付款人账号（PAN）。
     * Type: [0-9], Length: 1-19, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 19)
    private String s16;

    /**
     * 付款人参考号（商户ID等唯一标识）；南非相关 AFT 交易必填。
     * Type: [a-zA-Z0-9], Length: 1-16, Stand-Alone Operations: [1][2][6], Required: c.
     */
    @Size(min = 1, max = 16)
    private String s17;

    /**
     * 付款资金来源：01信用卡、02借记卡、03预付卡、04现金、05借记/存款账户、06信用账户、25移动钱包。
     * Type: [0-9], Length: 2-2, Stand-Alone Operations: [1][2][6], Required: c（AFT 必填；美国 AFT 且 j9=US 时为 m）。
     */
    @Size(min = 2, max = 2)
    private String s18;

    /**
     * 付款人生日（MMDDYYYY）。
     * Type: [0-9], Length: 8-8, Stand-Alone Operations: [1][2][6], Required: c（AFT [1]/[2] 必填）。
     */
    @Size(min = 8, max = 8)
    private String s19;

    /**
     * 路由处理器代码，指定交易处理路由。
     * Type: [a-zA-Z0-9], Length: 0-9, Stand-Alone Operations: [1][2][6], Required: o（[6] 不适用）。
     */
    @Size(max = 9)
    private String r1;

    /**
     * 路由目标MID。
     * Type: [a-zA-Z0-9], Length: 0-32, Stand-Alone Operations: [1][2][6], Required: o（[6] 不适用）。
     */
    @Size(max = 32)
    private String r2;

    /**
     * 路由顺序号。
     * Type: [1-9], Length: 1-2, Stand-Alone Operations: [1][2][6], Required: o.
     */
    @Size(min = 1, max = 2)
    private String r3;

    /**
     * 是否发起 Shift4 3D Secure：01 先3DS再支付，02 不发起（默认），03 按 3DS Adviser，04 仅 Adviser（op 98）。
     * Type: [0-3], Length: 2-2, Stand-Alone Operations: [1][2][6], Required: o（与 i8 同时存在会被拒绝）。
     */
    @JsonProperty("3ds_initiate")
    @Size(min = 2, max = 2)
    private String threeDsInitiate;

    /**
     * 令牌解密返回的 ECI 值。
     * Type: [0-9], Length: 1-2, Stand-Alone Operations: [1][2][6], Required: c（若交易为 scheme token 且发送 b21）。
     */
    @Size(min = 1, max = 2)
    @JsonProperty("token_eci")
    private String tokenEci;

    /**
     * 令牌解密返回的 CAVV/AAV 值。
     * Type: [A-Za-z0-9], Length: 0-40, Stand-Alone Operations: [1][2][6], Required: c（scheme token 且发送 b21 时必填）。
     */
    @Size(max = 40)
    @JsonProperty("token_crypto")
    private String tokenCrypto;

    /**
     * 加密货币类型：1 CBDC、2 稳定币、3 原生链币、4 NFT、7 其他。
     * Type: [0-9], Length: 1-1, Stand-Alone Operations: [1][2][6], Required: c（Ramp Provider/Conversion Affiliate 交易必填）。
     */
    @Size(min = 1, max = 1)
    @JsonProperty("crypto_currency_type")
    private String cryptoCurrencyType;
}
