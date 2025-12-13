package com.myruoyi.common.core.exception;

import com.myruoyi.common.core.result.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public BusinessException() {
    }

    public BusinessException(String message) {
        this.message = message;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(String message, Integer code, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, String detailMessage) {
        this.message = message;
        this.detailMessage = detailMessage;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(String message, String detailMessage, Integer code) {
        this.message = message;
        this.detailMessage = detailMessage;
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        this.message = resultCode.getMsg();
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String detailMessage) {
        this.message = resultCode.getMsg();
        this.detailMessage = detailMessage;
        this.code = resultCode.getCode();
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", detailMessage='" + detailMessage + '\'' +
                '}';
    }
}