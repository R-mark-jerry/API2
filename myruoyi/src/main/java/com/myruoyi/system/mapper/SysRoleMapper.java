package com.myruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.system.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据角色键查询角色
     *
     * @param roleKey 角色键
     * @return 角色对象
     */
    SysRole selectRoleByKey(@Param("roleKey") String roleKey);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 检查用户角色是否存在
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 数量
     */
    int checkUserRoleExist(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 新增用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 结果
     */
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 删除用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 批量删除用户角色关联
     *
     * @param userIds 用户ID数组
     * @return 结果
     */
    int deleteUserRoleByUserIds(@Param("userIds") Long[] userIds);
}