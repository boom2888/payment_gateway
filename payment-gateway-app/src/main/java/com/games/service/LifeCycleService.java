package com.games.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.games.common.utils.DateUtils;
import com.games.payment.service.IOrderService;
import com.games.tg.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class LifeCycleService {
    /** API服务接口 */
    @Value("${api.baseUrl}")
    private String apiUrl;
    /** ADMIN服务接口 */
    @Value("${admin.baseUrl}")
    private String adminUrl;
    /** 健康检查开关 */
    @Value("${tg.safeCheck}")
    private Boolean safeCheck;
    /** 订单检查时间 >0 才检查 */
    @Value("${tg.orderCheck}")
    private Integer orderCheck;
    /** 健康检查TG群组ID */
    @Value("${tg.safeGroupId}")
    private String safeGroupId;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private IOrderService orderService;

    public void checkApi(){
        if(!safeCheck)return;
        boolean isApiOk = isLifeCycleOk(apiUrl);
        boolean isAdminOk = isLifeCycleOk(adminUrl);
        StringBuilder sb = new StringBuilder();
        if(!isApiOk){
            sb.append("接口服务健康检查失败!").append("\r\n");
        }
        if(!isAdminOk){
            sb.append("后台服务健康检查失败!").append("\r\n");
        }
        if(StrUtil.isNotBlank(sb.toString())){
            telegramService.pushMessage(safeGroupId, sb.toString());
        }
    }

    public void checkNewOrder(){
        //超过20分钟没有新订单的检查
        if(orderCheck != null && orderCheck > 1){
            Date latestOrderDate = orderService.getLatestOrder(null);
            Date standDate = new Date();
            long noOderMinutes = DateUtils.getMinutesBetween(latestOrderDate, standDate);
//            log.info("最后一笔订单时间:{},当前时间:{},差多少分钟:{}", latestOrderDate, standDate, noOderMinutes);
            if(noOderMinutes >= orderCheck){
                String info = StrUtil.format("超过{}分钟，没有新订单\r\n", noOderMinutes);
                telegramService.pushMessage(safeGroupId, info);
            }
        }
    }

    public boolean isLifeCycleOk(String fullUrl){
        try{
            String responce = HttpUtil.get(fullUrl);
            if (StrUtil.isNotBlank(responce)){
                JSONObject resObj = JSONUtil.parseObj(responce);
                Integer code = resObj.getInt("code");
                return 200 == code;
            }
        }catch (Exception e){
           log.error("访问接口失败:{}", fullUrl);
        }
        return false;
    }

}
