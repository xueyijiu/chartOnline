package com.zjc.friend.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 10:07 2020/4/10
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor()).
                excludePathPatterns("/manager/css/**")
                .excludePathPatterns("/friend/res/**")
                .excludePathPatterns("/friend/css/**")
                .excludePathPatterns("/friend/js/**")
                .excludePathPatterns("/friend/img/**")
                .excludePathPatterns("/userinfo/login")
                .excludePathPatterns("/userinfo/register")
                .excludePathPatterns("/manage/login")
                .excludePathPatterns("/manage/loginHtml")
                .excludePathPatterns("/loginInfo")
                .excludePathPatterns("/registerInfo")
                .excludePathPatterns("/error")
                .excludePathPatterns("/pic/**")
                .excludePathPatterns("/websocket/*")
                .excludePathPatterns("/res/**")
                .excludePathPatterns("/manager/js/**")
                .excludePathPatterns("/manager/my_js/**")
                .excludePathPatterns("/manage/comment/intercept")
                .addPathPatterns("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/loginInfo").setViewName("/friend/html/login");
        registry.addViewController("/").setViewName("/friend/html/top");
        registry.addViewController("/registerInfo").setViewName("/friend/html/register");
        registry.addViewController("/manage/loginHtml").setViewName("/manage/login");
        registry.addViewController("/manage").setViewName("/manage/index");
        registry.addViewController("/manage/register_page").setViewName("/manage/register");
        registry.addViewController("/manage/control-table/updateViolation2").setViewName("/manage/update_violation");
        registry.addViewController("/manage/control-table/updateViolation1").setViewName("/manage/update_violation");
        registry.addViewController("/manage/change_user_role").setViewName("/manage/change_user_role");
        registry.addViewController("/manage/display_dynamic_info").setViewName("/manage/dynamic");
        registry.addViewController("/manage/control-table/violation1").setViewName("/manage/violation");
    }

}
