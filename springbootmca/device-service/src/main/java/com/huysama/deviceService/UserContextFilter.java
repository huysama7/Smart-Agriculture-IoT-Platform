package com.huysama.deviceService;

import com.huysama.builderDto.config.UserContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Đọc từ header
        String userId = request.getHeader("X-User-Id");
        String orgId = request.getHeader("X-Org-Id");
        System.out.println("User ID: " + userId);
        UserContextHolder.setCurrentUserId(userId);
        UserContextHolder.setCurrentOrgId(orgId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }
}
