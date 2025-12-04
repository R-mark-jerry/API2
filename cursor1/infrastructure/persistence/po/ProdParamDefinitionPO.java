package infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 自定义指标参数表实体类
 * 对应数据库表：prod_param_definition
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prod_param_definition")
public class ProdParamDefinitionPO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键，唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 参数标识（英文），如：volume
     */
    @TableField("param_key")
    private String paramKey;
    
    /**
     * 参数中文名称，如：矿卡容量
     */
    @TableField("param_name")
    private String paramName;
    
    /**
     * 参数默认值
     */
    @TableField("param_value")
    private String paramValue;
    
    /**
     * 参数单位，m³、吨等
     */
    @TableField("param_unit")
    private String paramUnit;
    
    /**
     * 参数说明
     */
    @TableField("description")
    private String description;
    
    /**
     * 自定义参数作用范围：1对应生产记录；2对应生产报表
     */
    @TableField("scope")
    private Integer scope;
    
    /**
     * 参数是否启用：0对应不启用；1对应启用
     */
    @TableField("enabled")
    private Integer enabled;
    
    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}

