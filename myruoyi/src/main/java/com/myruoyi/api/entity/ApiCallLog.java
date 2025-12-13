package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * API调用记录实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@TableName("api_call_log")
@Schema(description = "API调用记录")
public class ApiCallLog {

    private static final long serialVersionUID = 1L;

    @TableId(value = "call_id", type = IdType.AUTO)
    @Schema(description = "调用记录ID")
    private Long callId;

    @TableField("api_id")
    @Schema(description = "API ID")
    private Long apiId;

    @TableField("api_code")
    @Schema(description = "API编码")
    private String apiCode;

    @TableField("call_user_id")
    @Schema(description = "调用用户ID")
    private Long callUserId;

    @TableField("call_user_name")
    @Schema(description = "调用用户名")
    private String callUserName;

    @TableField("call_ip")
    @Schema(description = "调用IP")
    private String callIp;

    @TableField("request_method")
    @Schema(description = "请求方法")
    private String requestMethod;

    @TableField("request_url")
    @Schema(description = "请求URL")
    private String requestUrl;

    @TableField("request_params")
    @Schema(description = "请求参数")
    private String requestParams;

    @TableField("response_status")
    @Schema(description = "响应状态码")
    private Integer responseStatus;

    @TableField("response_data")
    @Schema(description = "响应数据")
    private String responseData;

    @TableField("response_time")
    @Schema(description = "响应时间（毫秒）")
    private Long responseTime;

    @TableField("call_result")
    @Schema(description = "调用结果（0成功 1失败）")
    private String callResult;

    @TableField("error_message")
    @Schema(description = "错误信息")
    private String errorMessage;

    @TableField("call_time")
    @Schema(description = "调用时间")
    private LocalDateTime callTime;

    /**
     * API名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "API名称")
    private String apiName;
}