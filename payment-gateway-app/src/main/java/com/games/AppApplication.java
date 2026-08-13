package com.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author lor
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableRetry
public class AppApplication {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AppApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  若依App启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
