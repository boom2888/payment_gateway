package com.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author lor
 */
@SpringBootApplication
@EnableAsync
@EnableRetry
public class AdminApplication {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Lor启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
