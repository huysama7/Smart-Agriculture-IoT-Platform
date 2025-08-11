package com.huysama.authenticationService.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DynamicPermissionFilter extends OncePerRequestFilter {

//    @Autowired
//    private StringRedisTemplate redisTemplate;

//    @Autowired
//    private PermissionService permissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        String userId = request.getUserPrincipal().getName();

        String apiKey = String.format("API_PERMISSION:%s:%s", method, path);
//        String requiredPermission = redisTemplate.opsForValue().get(apiKey);

//        if (requiredPermission == null) {
//            // fallback DB
//            requiredPermission = permissionService.findPermissionForApi(path, method);
//            if (requiredPermission != null) {
//                redisTemplate.opsForValue().set(apiKey, requiredPermission);
//            }
//        }
//
//        // 2. Kiểm tra quyền của user trong Redis
//        if (requiredPermission != null) {
//            String userKey = String.format("USER_PERMISSIONS:%s", userId);
//            Boolean hasPermission = redisTemplate.opsForSet().isMember(userKey, requiredPermission);
//
//            if (hasPermission == null || !hasPermission) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                return;
//            }
//        }

        filterChain.doFilter(request, response);
    }
}
