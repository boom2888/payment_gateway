package com.games.task;

import com.games.common.utils.DateUtils;
import com.games.service.LifeCycleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component("lifeCycleTask")
@EnableScheduling
public class LifeCycleTask {
    @Autowired
    LifeCycleService lifeCycleService;

    private final AtomicInteger apiCounter = new AtomicInteger(0);
    private final AtomicInteger orderCounter = new AtomicInteger(0);

    // 每分钟检查服务是否正常
    @Scheduled(fixedDelay = 60 * 1000)
    public void checkApiTask() {
        lifeCycleService.checkApi();

        int current = apiCounter.incrementAndGet();
        if (current % 10 == 0) {
            log.info("checkApiTask 已执行 {} 次", current);
        }
    }

    //每10分钟检查是否有新订单
    @Scheduled(fixedDelay =  60 * 1000)
    public void checkNewOrderTask() {
        lifeCycleService.checkNewOrder();
        int current = orderCounter.incrementAndGet();
        if (current % 3 == 0) {
            log.info("checkNewOrderTask 已执行 {} 次, 当前时间:{}", current, DateUtils.getTime());
        }
    }
}
