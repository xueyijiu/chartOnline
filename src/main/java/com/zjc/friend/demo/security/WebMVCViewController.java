package com.zjc.friend.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 12:19 2020/3/28
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
@Configuration
public class WebMVCViewController extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/loginInfo").setViewName("html/login");
        registry.addViewController("/registerInfo").setViewName("html/register");
    }
}
