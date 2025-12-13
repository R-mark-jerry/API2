package com.myruoyi.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    ERROR(500, "操作失败"),

    /**
     * 警告
     */
    WARN(401, "操作警告"),

    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(400, "参数检验失败"),

    /**
     * 暂未登录或token已经过期
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),

    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不允许
     */
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 系统繁忙
     */
    SYSTEM_BUSY(429, "系统繁忙，请稍后再试"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "系统错误"),

    /**
     * 系统维护中
     */
    SYSTEM_MAINTENANCE(503, "系统维护中"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(600, "业务异常"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(601, "数据不存在"),

    /**
     * 数据已存在
     */
    DATA_EXISTS(602, "数据已存在"),

    /**
     * 数据状态异常
     */
    DATA_STATUS_ERROR(603, "数据状态异常"),

    /**
     * 操作频繁
     */
    OPERATION_FREQUENT(604, "操作过于频繁，请稍后再试"),

    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(605, "验证码错误"),

    /**
     * 验证码过期
     */
    CAPTCHA_EXPIRED(606, "验证码已过期"),

    /**
     * 用户名或密码错误
     */
    USERNAME_PASSWORD_ERROR(607, "用户名或密码错误"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(608, "用户已被禁用"),

    /**
     * 用户已被锁定
     */
    USER_LOCKED(609, "用户已被锁定"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(610, "用户不存在"),

    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(611, "角色不存在"),

    /**
     * 菜单不存在
     */
    MENU_NOT_FOUND(612, "菜单不存在"),

    /**
     * 部门不存在
     */
    DEPT_NOT_FOUND(613, "部门不存在"),

    /**
     * API不存在
     */
    API_NOT_FOUND(614, "API不存在"),

    /**
     * API状态异常
     */
    API_STATUS_ERROR(615, "API状态异常"),

    /**
     * API权限不足
     */
    API_PERMISSION_DENIED(616, "API权限不足"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(700, "文件上传失败"),

    /**
     * 文件下载失败
     */
    FILE_DOWNLOAD_ERROR(701, "文件下载失败"),

    /**
     * 文件格式不支持
     */
    FILE_FORMAT_NOT_SUPPORTED(702, "文件格式不支持"),

    /**
     * 文件大小超出限制
     */
    FILE_SIZE_EXCEEDED(703, "文件大小超出限制"),

    /**
     * 网络异常
     */
    NETWORK_ERROR(800, "网络异常"),

    /**
     * 数据库异常
     */
    DATABASE_ERROR(801, "数据库异常"),

    /**
     * 缓存异常
     */
    CACHE_ERROR(802, "缓存异常"),

    /**
     * 消息队列异常
     */
    MQ_ERROR(803, "消息队列异常"),

    /**
     * 第三方服务异常
     */
    THIRD_PARTY_ERROR(804, "第三方服务异常");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 返回消息
     */
    private final String msg;
}