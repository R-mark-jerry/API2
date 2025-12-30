package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.myruoyi.common.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * API模块实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("api_module")
@Schema(description = "API模块")
public class ApiModule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "module_id", type = IdType.AUTO)
    @Schema(description = "模块ID")
    private Long moduleId;

    @TableField("app_id")
    @Schema(description = "应用ID")
    private Long appId;

    @TableField("module_code")
    @Schema(description = "模块编码")
    private String moduleCode;

    @TableField("module_name")
    @Schema(description = "模块名称")
    private String moduleName;

    @TableField("module_desc")
    @Schema(description = "模块描述")
    private String moduleDesc;

    @TableField("parent_id")
    @Schema(description = "父模块ID")
    private Long parentId;

    @TableField("order_num")
    @Schema(description = "显示顺序")
    private Integer orderNum;

    @TableField("deleted")
    @TableLogic
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
     * 应用名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 子模块列表（非数据库字段，用于树形结构）
     */
    @TableField(exist = false)
    @Schema(description = "子模块列表")
    private List<ApiModule> children;
}