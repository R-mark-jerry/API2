package com.myruoyi.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录DTO
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@Schema(description = "登录请求参数")
public class LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456")
    private String password;

    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "1234")
    private String code;

    /**
     * 验证码key
     */
    @Schema(description = "验证码key", example = "uuid")
    private String uuid;
}