package com.myruoyi.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myruoyi.common.core.result.Result;
import com.myruoyi.common.core.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT认证入口点
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                     AuthenticationException authException) throws IOException, ServletException {
        
        log.debug("未授权访问: {}", request.getRequestURI());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Result<Object> result = Result.error(ResultCode.UNAUTHORIZED);
        
        ObjectMapper mapper = new ObjectMapper();
        // 禁用WRITE_DATES_AS_TIMESTAMPS，避免将日期写为时间戳
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValue(response.getOutputStream(), result);
    }
}