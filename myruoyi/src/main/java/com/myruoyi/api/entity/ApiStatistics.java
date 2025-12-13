package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * API统计实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@TableName("api_statistics")
@Schema(description = "API统计")
public class ApiStatistics {

    private static final long serialVersionUID = 1L;

    @TableId(value = "stat_id", type = IdType.AUTO)
    @Schema(description = "统计ID")
    private Long statId;

    @TableField("api_id")
    @Schema(description = "API ID")
    private Long apiId;

    @TableField("api_code")
    @Schema(description = "API编码")
    private String apiCode;

    @TableField("stat_date")
    @Schema(description = "统计日期")
    private LocalDate statDate;

    @TableField("total_calls")
    @Schema(description = "总调用次数")
    private Long totalCalls;

    @TableField("success_calls")
    @Schema(description = "成功调用次数")
    private Long successCalls;

    @TableField("fail_calls")
    @Schema(description = "失败调用次数")
    private Long failCalls;

    @TableField("avg_response_time")
    @Schema(description = "平均响应时间（毫秒）")
    private BigDecimal avgResponseTime;

    @TableField("max_response_time")
    @Schema(description = "最大响应时间（毫秒）")
    private Long maxResponseTime;

    @TableField("min_response_time")
    @Schema(description = "最小响应时间（毫秒）")
    private Long minResponseTime;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * API名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "API名称")
    private String apiName;

    /**
     * 成功率（非数据库字段，用于计算）
     */
    @TableField(exist = false)
    @Schema(description = "成功率")
    private BigDecimal successRate;
}