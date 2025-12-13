package com.myruoyi.common.core.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一响应体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     */
    public Result() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return Result.success(ResultCode.SUCCESS.getMsg());
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return Result.success(ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static <T> Result<T> success(String msg) {
        return Result.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> Result<T> warn(String msg) {
        return Result.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> Result<T> warn(String msg, T data) {
        return new Result<>(ResultCode.WARN.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static <T> Result<T> error() {
        return Result.error(ResultCode.ERROR.getMsg());
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> Result<T> error(String msg) {
        return Result.error(ResultCode.ERROR.getCode(), msg);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param resultCode 业务错误码
     * @return 警告消息
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    /**
     * 方便链式调用
     *
     * @param msg
     * @return
     */
    public Result<T> msg(String msg) {
        setMsg(msg);
        return this;
    }

    /**
     * 方便链式调用
     *
     * @param code
     * @return
     */
    public Result<T> code(Integer code) {
        setCode(code);
        return this;
    }

    /**
     * 方便链式调用
     *
     * @param data
     * @return
     */
    public Result<T> data(T data) {
        setData(data);
        return this;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 是否失败
     *
     * @return
     */
    public boolean isError() {
        return !isSuccess();
    }
}