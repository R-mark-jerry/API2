package com.myruoyi.system.service.impl;

import com.myruoyi.common.core.exception.BusinessException;
import com.myruoyi.common.core.result.ResultCode;
import com.myruoyi.common.utils.JwtUtils;
import com.myruoyi.system.dto.LoginDTO;
import com.myruoyi.system.dto.LoginUserDTO;
import com.myruoyi.system.entity.SysUser;
import com.myruoyi.system.mapper.SysUserMapper;
import com.myruoyi.system.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SysUserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_INFO_KEY_PREFIX = "user:info:";
    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";

    @Override
    public LoginUserDTO login(LoginDTO loginDTO) {
        // 用户认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        // 获取用户信息
        String username = authentication.getName();
        SysUser user = userMapper.selectUserByUserName(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 查询用户角色和权限
        List<String> roles = userMapper.selectRolesByUserId(user.getUserId());
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getUserId());
        String deptName = userMapper.selectDeptNameByUserId(user.getUserId());

        // 生成JWT令牌
        String token = jwtUtils.generateToken(username, user.getUserId(), username);

        // 构建登录用户信息
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        BeanUtils.copyProperties(user, loginUserDTO);
        loginUserDTO.setToken(token);
        loginUserDTO.setRoles(roles);
        loginUserDTO.setPermissions(permissions);
        loginUserDTO.setDeptName(deptName);
        loginUserDTO.setDeptId(user.getDeptId());

        // 计算过期时间
        Date expirationDate = jwtUtils.getExpirationDateFromToken(token);
        loginUserDTO.setExpireTime(expirationDate.getTime());

        // 缓存用户信息
        String userInfoKey = USER_INFO_KEY_PREFIX + user.getUserId();
        String tokenKey = USER_TOKEN_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(userInfoKey, loginUserDTO, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(tokenKey, user.getUserId(), 24, TimeUnit.HOURS);

        log.info("用户 {} 登录成功", username);
        return loginUserDTO;
    }

    @Override
    public void logout() {
        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }

        String username = authentication.getName();
        SysUser user = userMapper.selectUserByUserName(username);
        if (user != null) {
            // 清除缓存
            String userInfoKey = USER_INFO_KEY_PREFIX + user.getUserId();
            redisTemplate.delete(userInfoKey);
        }

        // 清除安全上下文
        SecurityContextHolder.clearContext();
        log.info("用户 {} 登出成功", username);
    }

    @Override
    public LoginUserDTO refresh() {
        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        String username = authentication.getName();
        SysUser user = userMapper.selectUserByUserName(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 查询用户角色和权限
        List<String> roles = userMapper.selectRolesByUserId(user.getUserId());
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getUserId());
        String deptName = userMapper.selectDeptNameByUserId(user.getUserId());

        // 生成新的JWT令牌
        String token = jwtUtils.generateToken(username, user.getUserId(), username);

        // 构建登录用户信息
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        BeanUtils.copyProperties(user, loginUserDTO);
        loginUserDTO.setToken(token);
        loginUserDTO.setRoles(roles);
        loginUserDTO.setPermissions(permissions);
        loginUserDTO.setDeptName(deptName);
        loginUserDTO.setDeptId(user.getDeptId());

        // 计算过期时间
        Date expirationDate = jwtUtils.getExpirationDateFromToken(token);
        loginUserDTO.setExpireTime(expirationDate.getTime());

        // 更新缓存
        String userInfoKey = USER_INFO_KEY_PREFIX + user.getUserId();
        String tokenKey = USER_TOKEN_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(userInfoKey, loginUserDTO, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(tokenKey, user.getUserId(), 24, TimeUnit.HOURS);

        log.info("用户 {} 刷新令牌成功", username);
        return loginUserDTO;
    }

    @Override
    public LoginUserDTO getUserInfo() {
        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        String username = authentication.getName();
        SysUser user = userMapper.selectUserByUserName(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 查询用户角色和权限
        List<String> roles = userMapper.selectRolesByUserId(user.getUserId());
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getUserId());
        String deptName = userMapper.selectDeptNameByUserId(user.getUserId());

        // 构建登录用户信息
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        BeanUtils.copyProperties(user, loginUserDTO);
        loginUserDTO.setRoles(roles);
        loginUserDTO.setPermissions(permissions);
        loginUserDTO.setDeptName(deptName);
        loginUserDTO.setDeptId(user.getDeptId());

        return loginUserDTO;
    }
}