package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.myruoyi.common.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API权限实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("api_permission")
@Schema(description = "API权限")
public class ApiPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "permission_id", type = IdType.AUTO)
    @Schema(description = "权限ID")
    private Long permissionId;

    @TableField("api_id")
    @Schema(description = "API ID")
    private Long apiId;

    @TableField("permission_type")
    @Schema(description = "权限类型（1角色 2用户）")
    private String permissionType;

    @TableField("permission_target")
    @Schema(description = "权限目标（角色ID或用户ID）")
    private Long permissionTarget;

    @TableField("permission_scope")
    @Schema(description = "权限范围（1调用 2调试 3管理）")
    private String permissionScope;

    /**
     * 删除标志（覆盖父类字段，标记为非数据库字段）
     */
    @TableField(exist = false)
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Integer deleted;
    
    @TableField("responsible_user_id")
    @Schema(description = "负责人ID")
    private Long responsibleUserId;

    @TableField("responsible_user_name")
    @Schema(description = "负责人姓名")
    private String responsibleUserName;

    /**
     * 备注（覆盖父类字段，标记为非数据库字段）
     */
    @TableField(exist = false)
    @Schema(description = "备注")
    private String remark;
    
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

    /**
     * API编码（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "API编码")
    private String apiCode;

    /**
     * API名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "API名称")
    private String apiName;

    /**
     * 目标名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "目标名称")
    private String targetName;
}