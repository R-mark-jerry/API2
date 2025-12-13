package com.myruoyi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Knife4j配置类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Configuration
public class Knife4jConfig {

    /**
     * 应用名称
     */
    @Value("${knife4j.openapi.title}")
    private String title;

    /**
     * 应用描述
     */
    @Value("${knife4j.openapi.description}")
    private String description;

    /**
     * 应用版本
     */
    @Value("${knife4j.openapi.version}")
    private String version;

    /**
     * 联系人信息
     */
    @Value("${knife4j.openapi.concat}")
    private String concat;

    /**
     * 联系人邮箱
     */
    @Value("${knife4j.openapi.email}")
    private String email;

    /**
     * API分组
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("1. 系统管理")
                .pathsToMatch("/system/**")
                .build();
    }

    /**
     * API分组
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("2. API管理")
                .pathsToMatch("/api/**")
                .build();
    }

    /**
     * API分组
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("3. 认证授权")
                .pathsToMatch("/auth/**")
                .build();
    }

    /**
     * API分组
     */
    @Bean
    public GroupedOpenApi monitorApi() {
        return GroupedOpenApi.builder()
                .group("4. 系统监控")
                .pathsToMatch("/monitor/**")
                .build();
    }

    /**
     * OpenAPI配置
     */
    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                .name(concat)
                                .email(email))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}