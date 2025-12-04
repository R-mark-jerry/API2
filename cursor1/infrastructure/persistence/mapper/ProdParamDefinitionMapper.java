package infrastructure.persistence.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import infrastructure.persistence.po.ProdParamDefinitionPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自定义指标参数表Mapper接口
 * 继承MyBatis-Plus-Join的MPJBaseMapper，提供基础CRUD操作和联表查询
 */
@Mapper
public interface ProdParamDefinitionMapper extends MPJBaseMapper<ProdParamDefinitionPO> {

}

