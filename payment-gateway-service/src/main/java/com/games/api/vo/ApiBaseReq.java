package com.games.api.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.games.common.exception.BusinessException;
import com.games.common.utils.SignUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Map;

@Data
public class ApiBaseReq {
    @NotBlank(message = "{merchant.id.not.empty}")
    @ApiModelProperty(value = "Merchant ID, use 1 for testing", required = true, example = "1")
    private String merchantId;

    @NotBlank(message = "{signature.not.empty}")
    @ApiModelProperty(value = "Signature. Generate by hashing ssoOrderId with merchant private key (SHA-256)", required = true, example = "TEST")
    private String sign;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public String getSignParams() {
        return "";
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public Map<String, Object> getParams() {
        Map<String, Object> logicParam = BeanUtil.beanToMap(this, false, true);
        // 遍历所有 key-value 进行处理
        logicParam.replaceAll((k, v) -> (v instanceof Date) ? DateUtil.format((Date) v, "yyyy-MM-dd HH:mm:ss") : v);
        return logicParam;
    }

    /**
     * 参数校验
     * @param isApi
     */
    public void check(boolean isApi){}

    @JsonIgnore
    public String checkSign(String appKey){
        String signCheck = SignUtil.sign(appKey, getSignParams());
        return signCheck;
    }

    public void check(String appKey, boolean isDev){
        String signCheck = checkSign(appKey);
        boolean b = StrUtil.equals(sign, signCheck);
        if(isDev){
            if(!b){
                throw new BusinessException("签名错误");
            }
        }else{
            if(!b){
                throw BusinessException.api("signature.error");
            }
        }
    }

}
