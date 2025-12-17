package com.myruoyi.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myruoyi.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象
     */
    SysUser selectUserByUserName(@Param("userName") String userName);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    SysUser selectUserById(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户部门名称
     *
     * @param userId 用户ID
     * @return 部门名称
     */
    String selectDeptNameByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户列表
     *
     * @param page        分页参数
     * @param userName    用户名
     * @param phonenumber 手机号
     * @param status      状态
     * @return 用户分页列表
     */
    IPage<SysUser> selectPage(Page<SysUser> page, @Param("userName") String userName,
                             @Param("phonenumber") String phonenumber, @Param("status") String status);
}