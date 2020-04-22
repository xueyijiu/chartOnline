package com.zjc.friend.demo.security;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 9:11 2020/3/8
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


/**
 * webscoket服务端配置
 *
 * @author asus
 */

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Autowired
    private MyPrincipalHandshakeHandler myDefaultHandshakeHandler;
    @Autowired
    private AuthHandshakeInterceptor sessionAuthHandshakeInterceptor;
    /**
     * 注册端点，发布或者订阅消息的时候需要连接此端点
     * setAllowedOrigins 非必须，*表示允许其他域进行连接
     * withSockJS  表示开始sockejs支持
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/endpoint-websocket")
                .addInterceptors(sessionAuthHandshakeInterceptor)
                .setHandshakeHandler(myDefaultHandshakeHandler)
                .setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置消息代理(中介)
     * enableSimpleBroker 服务端推送给客户端的路径前缀 ，可以设置多个
     * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/chat");
        registry.setUserDestinationPrefix("/user/");
        registry.setApplicationDestinationPrefixes("/app");
    }



}
