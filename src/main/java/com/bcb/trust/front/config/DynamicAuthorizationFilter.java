package com.bcb.trust.front.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bcb.trust.front.modules.system.model.entity.CatalogResourceEntity;
import com.bcb.trust.front.modules.system.model.repository.ResourceRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DynamicAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private ResourceRepository resourceRepository;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/",
        "/dashboard",
        "/landing",
        "/public/**",
        "/login",
        "/favicon.ico",
        "/.well-known",
        ".well-known"
    );

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String requestPath = request.getRequestURI();

        return PUBLIC_PATHS.stream()
                .anyMatch(path -> requestPath.startsWith(path) || requestPath.equals(path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod();

        System.out.println("Path: " + requestPath);
        System.out.println("Method: " + requestMethod);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            if (!hasAccess(authentication, requestPath, requestMethod)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access Denied");
                return;
            }
        }
    }

    private boolean hasAccess(Authentication authentication, String requestPath, String requestMethod) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        /*System.out.println("Authorities: " + authentication.getPrincipal());
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("authority: " + grantedAuthority.getAuthority());
        }*/

        List<CatalogResourceEntity> resources = resourceRepository.findByPathAndMethod(requestPath, requestMethod);

        for (CatalogResourceEntity catalogResourceEntity : resources) {
            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println("Authority: " + grantedAuthority.getAuthority());
                System.out.println("ResourceCode: " + catalogResourceEntity.getCode());
                if (grantedAuthority.getAuthority().equals(catalogResourceEntity.getCode())) {
                    return true;
                }
            }
        }

        return false;
    }
}
