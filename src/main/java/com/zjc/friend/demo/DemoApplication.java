package com.zjc.friend.demo;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zjc.friend.demo.util.VerifyServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SpringBootApplication
@MapperScan({"com.zjc.friend.demo.mapper"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * 注入验证码servlet
     */
    @Bean
    public ServletRegistrationBean indexServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new VerifyServlet());
        registration.addUrlMappings("/getVerifyCode");
        return registration;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        //.setReadTimeout(Duration.ofSeconds(300)).setConnectTimeout(Duration.ofSeconds(5))
        RestTemplate template = builder.build();
        List<HttpMessageConverter<?>> messageConverters = template.getMessageConverters();
        messageConverters.add(new FastJsonHttpMessageConverter());
        return template;
    }

    @Bean
    public HttpServletRequest httpServletRequest(HttpServletRequest httpServletRequest){
        return  httpServletRequest;
    }


}
