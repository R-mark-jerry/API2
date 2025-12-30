package com.myruoyi.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.myruoyi.common.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API接口实体
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("api_info")
@Schema(description = "API接口")
public class ApiInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "api_id", type = IdType.AUTO)
    @Schema(description = "API ID")
    private Long apiId;

    @TableField("app_id")
    @Schema(description = "应用ID")
    private Long appId;

    @TableField("module_id")
    @Schema(description = "模块ID")
    private Long moduleId;

    @TableField("api_code")
    @Schema(description = "API编码")
    private String apiCode;

    @TableField("api_name")
    @Schema(description = "API名称")
    private String apiName;

    @TableField("api_desc")
    @Schema(description = "API描述")
    private String apiDesc;

    @TableField("request_method")
    @Schema(description = "请求方法")
    private String requestMethod;

    @TableField("request_url")
    @Schema(description = "请求URL")
    private String requestUrl;

    @TableField("content_type")
    @Schema(description = "内容类型")
    private String contentType;

    @TableField("request_params")
    @Schema(description = "请求参数")
    private String requestParams;

    @TableField("response_params")
    @Schema(description = "响应参数")
    private String responseParams;

    @TableField("request_example")
    @Schema(description = "请求示例")
    private String requestExample;

    @TableField("response_example")
    @Schema(description = "响应示例")
    private String responseExample;

    @TableField("api_status")
    @Schema(description = "API状态（0开发中 1测试中 2已发布 3已下线）")
    private String apiStatus;

    @TableField("publish_status")
    @Schema(description = "发布状态（0未发布 1已发布 2灰度发布）")
    private String publishStatus;

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

    @TableField("deleted")
    @TableLogic
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Integer deleted;

    /**
     * 应用名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 模块名称（非数据库字段，用于关联查询）
     */
    @TableField(exist = false)
    @Schema(description = "模块名称")
    private String moduleName;
}