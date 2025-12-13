package com.myruoyi.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 登录用户DTO
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@Schema(description = "登录用户信息")
public class LoginUserDTO {

    /**
     * JWT令牌
     */
    @Schema(description = "JWT令牌")
    private String token;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickName;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phonenumber;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private String sex;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<String> roles;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private List<String> permissions;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    private Long expireTime;
}