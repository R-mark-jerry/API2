package com.myruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myruoyi.common.core.exception.BusinessException;
import com.myruoyi.common.core.result.ResultCode;
import com.myruoyi.common.utils.JwtUtils;
import com.myruoyi.system.entity.SysUser;
import com.myruoyi.system.mapper.SysUserMapper;
import com.myruoyi.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 用户服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_INFO_KEY_PREFIX = "user:info:";

    @Override
    public SysUser selectUserById(Long userId) {
        return baseMapper.selectUserById(userId);
    }

    @Override
    public String selectUserNameById(Long userId) {
        SysUser user = baseMapper.selectUserById(userId);
        return user != null ? user.getNickName() : null;
    }

    @Override
    public int insertUser(SysUser user) {
        // 检查用户名是否唯一
        if (!checkUserNameUnique(user)) {
            throw new BusinessException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        
        // 检查手机号是否唯一
        if (!checkPhoneUnique(user)) {
            throw new BusinessException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        
        // 检查邮箱是否唯一
        if (!checkEmailUnique(user)) {
            throw new BusinessException("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置部门ID为null，因为deptId字段不映射到数据库
        user.setDeptId(null);
        
        // 设置创建时间
        user.setCreateTime(LocalDateTime.now());
        
        return baseMapper.insert(user);
    }

    @Override
    public int updateUser(SysUser user) {
        // 检查用户名是否唯一
        if (!checkUserNameUnique(user)) {
            throw new BusinessException("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        
        // 检查手机号是否唯一
        if (!checkPhoneUnique(user)) {
            throw new BusinessException("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        
        // 检查邮箱是否唯一
        if (!checkEmailUnique(user)) {
            throw new BusinessException("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        
        return baseMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        // 删除用户与角色关联
        // TODO: 删除用户角色关联
        
        // 删除用户
        return baseMapper.deleteBatchIds(Arrays.asList(userIds));
    }

    @Override
    public int resetPwd(SysUser user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return baseMapper.updateById(user);
    }

    @Override
    public int changeStatus(SysUser user) {
        return baseMapper.updateById(user);
    }

    @Override
    public SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        String username = authentication.getName();
        return baseMapper.selectUserByUserName(username);
    }

    @Override
    public int updateProfile(SysUser user) {
        SysUser currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        user.setUserId(currentUser.getUserId());
        user.setUserName(null); // 不允许修改用户名
        user.setPassword(null); // 不允许修改密码
        
        return updateUser(user);
    }

    @Override
    public int updatePwd(String oldPassword, String newPassword) {
        SysUser currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        
        // 更新密码
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        return baseMapper.updateById(currentUser);
    }

    @Override
    public IPage<SysUser> selectUserPage(Page<SysUser> page, String userName, String phonenumber, String status) {
        return baseMapper.selectPage(page, userName, phonenumber, status);
    }

    /**
     * 校验用户名是否唯一
     *
     * @param user 用户信息
     * @return true 唯一 false 不唯一
     */
    private boolean checkUserNameUnique(SysUser user) {
        Long userId = user.getUserId() == null ? -1L : user.getUserId();
        SysUser info = baseMapper.selectUserByUserName(user.getUserName());
        if (info != null && info.getUserId().longValue() != userId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验手机号是否唯一
     *
     * @param user 用户信息
     * @return true 唯一 false 不唯一
     */
    private boolean checkPhoneUnique(SysUser user) {
        Long userId = user.getUserId() == null ? -1L : user.getUserId();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhonenumber, user.getPhonenumber())
               .ne(user.getUserId() != null, SysUser::getUserId, userId);
        
        return baseMapper.selectCount(wrapper) == 0;
    }

    /**
     * 校验邮箱是否唯一
     *
     * @param user 用户信息
     * @return true 唯一 false 不唯一
     */
    private boolean checkEmailUnique(SysUser user) {
        if (!StringUtils.hasText(user.getEmail())) {
            return true;
        }
        
        Long userId = user.getUserId() == null ? -1L : user.getUserId();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, user.getEmail())
               .ne(user.getUserId() != null, SysUser::getUserId, userId);
        
        return baseMapper.selectCount(wrapper) == 0;
    }
}