package com.sky.config;


import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 配置拦截器
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    private final JwtTokenUserInterceptor jwtTokenUserInterceptor;

    public WebMvcConfiguration(JwtTokenAdminInterceptor jwtTokenAdminInterceptor, JwtTokenUserInterceptor jwtTokenUserInterceptor) {
        this.jwtTokenAdminInterceptor = jwtTokenAdminInterceptor;
        this.jwtTokenUserInterceptor = jwtTokenUserInterceptor;
    }

    /**
     * 配置拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册管理端拦截器
        registry.addInterceptor(jwtTokenAdminInterceptor)
                // 配置拦截资源，拦截所有请求
                .addPathPatterns("/admin/**")
                // 排除不拦截的请求，这里是登录请求
                .excludePathPatterns("/admin/employee/login");

        // 注册用户端拦截器
        registry.addInterceptor(jwtTokenUserInterceptor)
                // 配置拦截资源，拦截所有请求
                .addPathPatterns("/user/**")
                // 排除不拦截的请求，这里是登录请求
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }
}