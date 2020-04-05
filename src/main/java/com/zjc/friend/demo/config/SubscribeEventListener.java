package com.zjc.friend.demo.config;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 12:19 2020/3/6
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 订阅事件监听器
 *
 * @author asus
 */
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("【SubscribeEventListener监听器事件 类型】" + headerAccessor.getCommand().getMessageType());

    }

}