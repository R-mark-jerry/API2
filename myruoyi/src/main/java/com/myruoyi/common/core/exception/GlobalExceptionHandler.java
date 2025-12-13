package com.myruoyi.common.core.exception;

import com.myruoyi.common.core.result.Result;
import com.myruoyi.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常，请求地址：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String errorMsg = String.format("请求参数缺失: %s", e.getParameterName());
        log.error("请求参数缺失，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
    }

    /**
     * 处理请求参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMsg = String.format("请求参数类型错误: %s", e.getName());
        log.error("请求参数类型错误，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorMsg = String.format("请求方法不支持: %s", e.getMethod());
        log.error("请求方法不支持，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.METHOD_NOT_ALLOWED.getCode(), errorMsg);
    }

    /**
     * 处理请求路径不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String errorMsg = String.format("请求路径不存在: %s", e.getRequestURL());
        log.error("请求路径不存在，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.NOT_FOUND.getCode(), errorMsg);
    }

    /**
     * 处理参数校验异常（@RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMsg = fieldErrors.isEmpty() ? "参数校验失败" : fieldErrors.get(0).getDefaultMessage();
        log.error("参数校验失败，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
    }

    /**
     * 处理参数校验异常（@ModelAttribute）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMsg = fieldErrors.isEmpty() ? "参数校验失败" : fieldErrors.get(0).getDefaultMessage();
        log.error("参数校验失败，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
    }

    /**
     * 处理参数校验异常（@Validated）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMsg = violations.isEmpty() ? "参数校验失败" : violations.iterator().next().getMessage();
        log.error("参数校验失败，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATE_FAILED.getCode(), errorMsg);
    }

    /**
     * 处理文件上传大小超出限制异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        String errorMsg = String.format("上传文件大小超出限制: %s", e.getMaxUploadSize());
        log.error("上传文件大小超出限制，请求地址：{}，异常信息：{}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.FILE_SIZE_EXCEEDED.getCode(), errorMsg);
    }

    /**
     * 处理数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        log.error("数据重复，请求地址：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.DATA_EXISTS.getCode(), "数据已存在，请检查唯一性约束");
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.error("认证失败，请求地址：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        if (e instanceof BadCredentialsException) {
            return Result.error(ResultCode.USERNAME_PASSWORD_ERROR);
        }
        return Result.error(ResultCode.UNAUTHORIZED);
    }

    /**
     * 处理授权异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("权限不足，请求地址：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.FORBIDDEN);
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常，请求地址：{}，异常信息：", request.getRequestURI(), e);
        return Result.error(ResultCode.SYSTEM_ERROR);
    }
}