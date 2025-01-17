package com.jiehfut.music_code.config;


import com.jiehfut.music_code.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    // 用于密码的 BCrypt 加密
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1、配置拦截规则
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        registry.addInterceptor(loginInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                //排除所有的JS
                .excludePathPatterns("/js/**.js")
                //排除images下所有的元素
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/css/**.css")
                .excludePathPatterns("/fronts/**")
                .excludePathPatterns("/player/**")
                .excludePathPatterns("/login.html")
                //排除登录接口
                .excludePathPatterns("/user/login");
    }



}
