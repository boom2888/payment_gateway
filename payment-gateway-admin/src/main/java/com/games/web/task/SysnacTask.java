package com.games.web.task;

import com.games.pay.service.TaskService;
import com.games.payment.TotalService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component("sysnacTask")
@EnableScheduling   // 2.开启定时任务
public class SysnacTask {
    @Autowired
    TaskService taskService;
    @Autowired
    TotalService totalShop;

//    @Scheduled(cron = "0 0 0 * * ?")
    public void resetShopLimit(){
        log.info("resetShopLimit task start: {}" , new Date());
        taskService.resetShopLimit();
    }

    //推送TG昨日收单统计信息
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "50 59 11,23 * * ?") //每天11点59分50秒和23点59分50秒
//    @Scheduled(fixedDelay = 60)
    public void pushShopTotalAmount(){
        log.info("pushShopTotalAmount task start: {}" , new Date());
        totalShop.pushShopTotalAmount();
    }



    //同步商户交易记录
//    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void reloadTradeCallBack() {
        taskService.reloadTradeCallBack();
    }
}
