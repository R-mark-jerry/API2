package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.myruoyi.common.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * API发布记录实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("api_publish_record")
@Schema(description = "API发布记录")
public class ApiPublishRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "publish_id", type = IdType.AUTO)
    @Schema(description = "发布记录ID")
    private Long publishId;

    @TableField("api_id")
    @Schema(description = "API ID")
    private Long apiId;

    @TableField("api_code")
    @Schema(description = "API编码")
    private String apiCode;

    @TableField("publish_type")
    @Schema(description = "发布类型（1正式发布 2灰度发布）")
    private String publishType;

    @TableField("publish_status")
    @Schema(description = "发布状态（0发布中 1发布成功 2发布失败）")
    private String publishStatus;

    @TableField("old_version")
    @Schema(description = "旧版本")
    private String oldVersion;

    @TableField("new_version")
    @Schema(description = "新版本")
    private String newVersion;

    @TableField("gray_rules")
    @Schema(description = "灰度规则（JSON格式）")
    private String grayRules;

    @TableField("publish_user_id")
    @Schema(description = "发布人ID")
    private Long publishUserId;

    @TableField("publish_user_name")
    @Schema(description = "发布人姓名")
    private String publishUserName;

    @TableField("publish_time")
    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @TableField("finish_time")
    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @TableField("rollback_time")
    @Schema(description = "回滚时间")
    private LocalDateTime rollbackTime;

    /**
     * API名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "API名称")
    private String apiName;
}