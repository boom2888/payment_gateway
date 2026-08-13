package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.games.api.enums.ApiStatus;
import com.games.api.enums.ApiType;
import com.games.common.annotation.Excel;
import com.games.system.domain.WithShopId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口日志对象 api_log
 *
 * @author ç½ä½³ç
 * @date 2024-06-03
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("api_log")
public class ApiLog extends WithShopId implements Serializable {

private static final long serialVersionUID=1L;

    public static ApiLog init(ApiType type, ApiStatus status, Long orderId, String ssoOrderId, String cardNo, String param, String response, Long shopId, String ip){
        ApiLog log = new ApiLog();
        log.setOrderId(orderId);
        log.setSsoOrderId(ssoOrderId);
        log.setMobile(cardNo);
        log.setParam(param);
        log.setResponse(response);
        log.setStatus(status.name());
        log.setApiType(type.name());
        log.setCreateTime(new Date());
        log.setShopId(shopId);
        log.setLocation(ip);
        return log;
    }

    /** 编号 */
    @TableId(value = "id")
    private Long id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long projectId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 供应商订单ID */
    @Excel(name = "供应商订单ID")
    private String ssoOrderId;

    /** 银行卡号 */
    @Excel(name = "银行卡号")
    private String mobile;

    /** 参数信息 */
    @Excel(name = "参数信息")
    private String param;

    /** 响应信息 */
    @Excel(name = "响应信息")
    private String response;

    private String remark;

    private String apiType;

    /** 请求状态 */
    @Excel(name = "请求状态")
    private String status;

    /** 备注 */
    @Excel(name = "位置")
    private String location;

    @TableField(exist = false)
    private String assignedLocation;
    @TableField(exist = false)
    String startTime;
    @TableField(exist = false)
    String endTime;

    @TableField(exist = false)
    private String company;
}
