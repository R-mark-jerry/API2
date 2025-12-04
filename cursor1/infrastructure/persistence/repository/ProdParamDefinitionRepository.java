package infrastructure.persistence.repository;

import infrastructure.persistence.po.ProdParamDefinitionPO;

import java.util.List;
import java.util.Optional;

/**
 * 自定义指标参数表仓储接口
 * 定义参数配置相关的业务操作方法
 */
public interface ProdParamDefinitionRepository {
    
    /**
     * 根据ID查询参数配置
     * @param id 主键ID
     * @return 参数配置对象，如果不存在返回Optional.empty()
     */
    Optional<ProdParamDefinitionPO> findById(Long id);
    
    /**
     * 根据参数键查询参数配置
     * @param paramKey 参数键
     * @return 参数配置对象，如果不存在返回Optional.empty()
     */
    Optional<ProdParamDefinitionPO> findByParamKey(String paramKey);
    
    /**
     * 根据作用范围查询启用的参数配置列表
     * @param scope 作用范围：1对应生产记录；2对应生产报表
     * @return 参数配置列表
     */
    List<ProdParamDefinitionPO> findByScope(Integer scope);
    
    /**
     * 查询所有启用的参数配置
     * @return 参数配置列表
     */
    List<ProdParamDefinitionPO> findAllEnabled();
    
    /**
     * 查询所有参数配置（包括禁用的）
     * @return 参数配置列表
     */
    List<ProdParamDefinitionPO> findAll();
    
    /**
     * 根据参数名称模糊查询
     * @param paramName 参数名称关键字
     * @return 参数配置列表
     */
    List<ProdParamDefinitionPO> findByParamNameLike(String paramName);
    
    /**
     * 保存参数配置（新增或更新）
     * @param paramsConfig 参数配置对象
     * @return 保存后的参数配置对象
     */
    ProdParamDefinitionPO save(ProdParamDefinitionPO paramsConfig);
    
    /**
     * 批量保存参数配置
     * @param paramsConfigs 参数配置列表
     * @return 保存后的参数配置列表
     */
    List<ProdParamDefinitionPO> saveAll(List<ProdParamDefinitionPO> paramsConfigs);
    
    /**
     * 根据参数键更新参数值
     * @param paramKey 参数键
     * @param paramValue 新的参数值
     * @return 更新后的参数配置对象，如果不存在返回Optional.empty()
     */
    Optional<ProdParamDefinitionPO> updateValueByParamKey(String paramKey, String paramValue);
    
    /**
     * 启用/禁用参数配置
     * @param id 主键ID
     * @param enabled 启用状态：0对应不启用；1对应启用
     * @return 更新后的参数配置对象，如果不存在返回Optional.empty()
     */
    Optional<ProdParamDefinitionPO> updateEnabledById(Long id, Integer enabled);
    
    /**
     * 根据ID删除参数配置
     * @param id 主键ID
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteById(Long id);
    
    /**
     * 根据参数键删除参数配置
     * @param paramKey 参数键
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteByParamKey(String paramKey);
    
    /**
     * 批量删除参数配置
     * @param ids 主键ID列表
     * @return 删除成功的数量
     */
    int deleteByIds(List<Long> ids);
    
    /**
     * 统计参数配置总数
     * @return 总数
     */
    long count();
    
    /**
     * 根据作用范围统计启用的参数配置数量
     * @param scope 作用范围
     * @return 数量
     */
    long countByScope(Integer scope);
    
    /**
     * 检查参数键是否存在
     * @param paramKey 参数键
     * @return 存在返回true，否则返回false
     */
    boolean existsByParamKey(String paramKey);
    
    /**
     * 检查参数配置是否存在
     * @param id 主键ID
     * @return 存在返回true，否则返回false
     */
    boolean existsById(Long id);
    
    /**
     * 根据作用范围检查是否存在启用的参数配置
     * @param scope 作用范围
     * @return 存在返回true，否则返回false
     */
    boolean existsByScope(Integer scope);
}

