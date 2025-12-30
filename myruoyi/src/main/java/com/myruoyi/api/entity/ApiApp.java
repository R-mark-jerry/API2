package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.myruoyi.common.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * API应用实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("api_app")
@Schema(description = "API应用")
public class ApiApp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "app_id", type = IdType.AUTO)
    @Schema(description = "应用ID")
    private Long appId;

    @TableField("app_code")
    @Schema(description = "应用编码")
    private String appCode;

    @TableField("app_name")
    @Schema(description = "应用名称")
    private String appName;

    @TableField("app_desc")
    @Schema(description = "应用描述")
    private String appDesc;

    @TableField("app_version")
    @Schema(description = "应用版本")
    private String appVersion;

    @TableField("owner_id")
    @Schema(description = "负责人ID")
    private Long ownerId;

    @TableField("owner_name")
    @Schema(description = "负责人姓名")
    private String ownerName;
    
    @TableField("responsible_user_id")
    @Schema(description = "负责人ID")
    private Long responsibleUserId;

    @TableField("responsible_user_name")
    @Schema(description = "负责人姓名")
    private String responsibleUserName;
    
    // 为了确保编译正确，手动添加getter和setter方法
    public Long getResponsibleUserId() {
        return responsibleUserId;
    }
    
    public void setResponsibleUserId(Long responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
    }
    
    public String getResponsibleUserName() {
        return responsibleUserName;
    }
    
    public void setResponsibleUserName(String responsibleUserName) {
        this.responsibleUserName = responsibleUserName;
    }

    @TableField("status")
    @Schema(description = "应用状态（0正常 1停用）")
    private String status;

    @TableField("deleted")
    @TableLogic
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Integer deleted;
}