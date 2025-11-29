package org.example.messageme.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class JwtTokenFilter extends OncePerRequestFilter {
//    private final JwtTokenProvider jwtTokenProvider;
//
////    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
////        this.jwtTokenProvider = jwtTokenProvider;
////    }
////
////    private static final List<String> WHITELIST = List.of(
////            "/api/v1/auth/register",
////            "/api/v1/auth/login",
////            "/api/v1/auth/verify-email",
////            "/v3/api-docs",
////            "/swagger-ui",
////            "/swagger-ui.html",
////            "/swagger-resources",
////            "/webjars"
////    );
////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain)
////            throws ServletException, IOException {
////
////        String path = request.getRequestURI();
////
////        // Логируем путь (временно, чтобы видеть в консоли)
////        System.out.println(">>> JwtTokenFilter: path = " + path);
////
////        // Пропускаем публичные пути (используем contains)
//////        if (WHITELIST.stream().anyMatch(path::contains)) {
//////            filterChain.doFilter(request, response);
//////            return;
//////        }
////        if (request.getRequestURI().startsWith("/api/v1/auth/")) {
////            filterChain.doFilter(request, response);
////            return;
////        }
////
////        // Проверяем JWT
////        String header = request.getHeader("Authorization");
////        if (header != null && header.startsWith("Bearer ")) {
////            String token = header.substring(7);
////            if (jwtTokenProvider.validateToken(token)) {
////                Authentication auth = jwtTokenProvider.getAuthentication(token);
////                SecurityContextHolder.getContext().setAuthentication(auth);
////            }
////        }
////
////        filterChain.doFilter(request, response);
////    }
//
//
////private static final List<String> WHITELIST = List.of(
////        "/api/v1/auth/",       // ← с / в конце
////        "/v3/api-docs/",
////        "/swagger-ui/",
////        "/swagger-ui.html",
////        "/swagger-resources/",
////        "/webjars/"
////);
////
////
//////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain)
////            throws ServletException, IOException {
////
////        String path = request.getRequestURI();
////
////        System.out.println(">>> JwtTokenFilter: path = " + path);
////
////        // Пропускаем публичные пути
////        for (String whitelistPath : WHITELIST) {
////            if (path.startsWith(whitelistPath)) {
////                filterChain.doFilter(request, response);
////                return;
////            }
////        }
////
////        // Проверяем JWT только для защищенных путей
////        String header = request.getHeader("Authorization");
////        if (header != null && header.startsWith("Bearer ")) {
////            String token = header.substring(7);
////            if (jwtTokenProvider.validateToken(token)) {
////                Authentication auth = jwtTokenProvider.getAuthentication(token);
////                SecurityContextHolder.getContext().setAuthentication(auth);
////            }
////        }
////
////        filterChain.doFilter(request, response);
////    }
//
//
//@Override
//protected void doFilterInternal(HttpServletRequest request,
//                                HttpServletResponse response,
//                                FilterChain filterChain) throws ServletException, IOException {
//
//    String header = request.getHeader("Authorization");
//
//    // Только если токен есть - проверяем и устанавливаем аутентификацию
//    if (header != null && header.startsWith("Bearer ")) {
//        String token = header.substring(7);
//        try {
//            if (jwtTokenProvider.validateToken(token)) {
//                Authentication auth = jwtTokenProvider.getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        } catch (Exception e) {
//            // Логируем ошибку, но не блокируем запрос
//            System.err.println("JWT validation error: " + e.getMessage());
//        }
//    }
//
//    filterChain.doFilter(request, response);
//}
//
////    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain)
////            throws ServletException, IOException {
////
////        // 1. Пропускаем preflight (OPTIONS)
////        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
////            response.setStatus(HttpServletResponse.SC_OK);
////            return;
////        }
////
////        String path = request.getRequestURI();
////
////        // 2. Белый список
////        for (String whitelistPath : WHITELIST) {
////            if (path.startsWith(whitelistPath)) {
////                filterChain.doFilter(request, response);
////                return;
////            }
////        }
////
////        // 3. Проверяем токен
////        String header = request.getHeader("Authorization");
////        if (header != null && header.startsWith("Bearer ")) {
////            String token = header.substring(7);
////            if (jwtTokenProvider.validateToken(token)) {
////                Authentication auth = jwtTokenProvider.getAuthentication(token);
////                SecurityContextHolder.getContext().setAuthentication(auth);
////            }
////        }
////
////        filterChain.doFilter(request, response);
////    }
//
//
//
//
////    @Override
////    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
////        HttpServletRequest request = (HttpServletRequest) servletRequest;
////        String path = request.getRequestURI();
////        if (WHITELIST.stream().anyMatch(path::startsWith)) {
////            filterChain.doFilter(request, servletResponse);
////            return;
////        }
////
////        String bearerToken = request.getHeader("Authorization");
////        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
////            bearerToken = bearerToken.substring(7);
////        }
////        if(bearerToken != null && jwtTokenProvider.validateToken(bearerToken)){
////            try{
////                Authentication auth = jwtTokenProvider.getAuthentication(bearerToken);
////                if(auth != null) {
////                    SecurityContextHolder.getContext().setAuthentication(auth);
////                }
////            }
////            catch(Exception e){
////                throw new ServletException(e);
////            }
////        }
////        filterChain.doFilter(servletRequest, servletResponse);
////
////    }
//}

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("========================================");
        System.out.println("=== FILTER START: " + path);
        System.out.println("=== Method: " + request.getMethod());

        String header = request.getHeader("Authorization");
        System.out.println("=== Authorization header: " + (header != null ? "EXISTS" : "NULL"));

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("=== Token extracted: " + token.substring(0, Math.min(20, token.length())) + "...");
            try {
                boolean isValid = jwtTokenProvider.validateToken(token);
                System.out.println("=== Token valid: " + isValid);

                if (isValid) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("=== Authentication set: " + auth.getName());
                } else {
                    System.out.println("=== Token validation FAILED");
                }
            } catch (Exception e) {
                System.err.println("=== JWT validation ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("=== No Bearer token in request");
        }

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== Current authentication: " + (currentAuth != null ? currentAuth.getName() : "NULL"));
        System.out.println("========================================");

        filterChain.doFilter(request, response);
    }
}
