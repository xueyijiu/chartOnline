package com.zjc.friend.demo.service.impl;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 9:17 2020/3/8
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.friend.demo.entity.ChatRecord;
import com.zjc.friend.demo.entity.InMessage;
import com.zjc.friend.demo.entity.OutMessage;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.mapper.ChatRecordMapper;
import com.zjc.friend.demo.mapper.UserinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 功能描述：简单消息模板，用来推送消息
 */
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private UserinfoMapper userMapper;

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    /**
     * 单人聊天
     *
     * @param message
     */
    public void sendChatMessage(InMessage message) {
        System.out.println(message);
        OutMessage outMessage = new OutMessage();
        outMessage.setContent(message.getContent());
        outMessage.setFrom(message.getFrom());
        outMessage.setTime(new Date());
        System.out.println(outMessage);

        ChatRecord record = new ChatRecord();
        record.setStatus(false);
        record.setMessage(message.getContent());
        record.setCreateTime(new Date());
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", message.getFrom());
        Userinfo userinfo = userMapper.selectOne(wrapper);
        QueryWrapper<Userinfo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("username", message.getTo());
        Userinfo userinfo1 = userMapper.selectOne(wrapper1);
        record.setUserId(userinfo.getId());
        record.setFriendId(userinfo1.getId());
        chatRecordMapper.insert(record);
        outMessage.setId(record.getId());
        template.convertAndSendToUser(message.getTo(), "/chat/single/",
                outMessage);
    }
}
