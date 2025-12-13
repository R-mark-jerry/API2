package com.myruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

/**
 * API全生命周期管理平台启动类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@SpringBootApplication(exclude = {
    JacksonAutoConfiguration.class
})
public class ApiLifecyclePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiLifecyclePlatformApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  API全生命周期管理平台启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}