package com.zjc.friend.demo.config;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 12:18 2020/3/6
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * 连接事件监听器，建立连接时候触发
 *
 * @author asus
 */
@Component
public class ConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("【ConnectEventListener监听器事件 类型】" + headerAccessor.getCommand().getMessageType());
    }

}