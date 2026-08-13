package com.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author lor
 */
@SpringBootApplication
@EnableScheduling
@EnableRetry
public class ApiApplication {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ApiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  若依App启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
