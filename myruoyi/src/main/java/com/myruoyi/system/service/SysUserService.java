package com.myruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myruoyi.system.entity.SysUser;

/**
 * 用户服务接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    SysUser selectUserById(Long userId);

    /**
     * 根据用户ID查询用户名
     *
     * @param userId 用户ID
     * @return 用户名
     */
    String selectUserNameById(Long userId);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    /**
     * 删除用户信息
     *
     * @param userIds 用户ID数组
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    int resetPwd(SysUser user);

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    int changeStatus(SysUser user);

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    SysUser getCurrentUser();

    /**
     * 修改当前用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateProfile(SysUser user);

    /**
     * 修改当前用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    int updatePwd(String oldPassword, String newPassword);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param userName 用户名
     * @param phonenumber 手机号
     * @param status 状态
     * @return 用户列表
     */
    IPage<SysUser> selectUserPage(Page<SysUser> page, String userName, String phonenumber, String status);
}