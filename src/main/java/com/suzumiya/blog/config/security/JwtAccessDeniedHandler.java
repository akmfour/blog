package com.suzumiya.blog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suzumiya.blog.common.ApiResponse;
import com.suzumiya.blog.common.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 當用戶嘗試訪問需要更高權限的資源時，返回 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 使用你自訂的 ApiResponse 格式返回錯誤訊息
        ApiResponse<Object> body = ApiResponse.fail(ResultCode.FORBIDDEN, "权限不足，无法访问此资源");

        // 使用 ObjectMapper 將物件轉換為 JSON 字串並寫入 response
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
