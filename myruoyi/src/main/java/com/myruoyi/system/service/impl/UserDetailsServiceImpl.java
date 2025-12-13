package com.myruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myruoyi.common.core.exception.BusinessException;
import com.myruoyi.system.entity.SysUser;
import com.myruoyi.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SysUser user = userMapper.selectUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        
        // 检查用户状态
        if ("1".equals(user.getStatus())) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 查询用户权限
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getUserId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            // 过滤掉空权限值
            if (permission != null && !permission.trim().isEmpty()) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        
        // 如果没有权限，添加一个默认权限
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        // 创建用户详情
        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}