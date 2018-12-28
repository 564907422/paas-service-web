package com.paas.web.config;

import com.paas.web.interceptors.CorsInterceptor;
import com.paas.web.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;
    @Autowired
    CorsInterceptor corsInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录验证拦截器
        InterceptorRegistration addInterceptor = registry.addInterceptor(loginInterceptor);

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/paas/manager/login");
        addInterceptor.excludePathPatterns("/paas/manager/loginout");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");

        //跨域拦截器
        InterceptorRegistration addCorsInterceptor = registry.addInterceptor(corsInterceptor);
        // 拦截配置
        addCorsInterceptor.addPathPatterns("/**");

    }
}
