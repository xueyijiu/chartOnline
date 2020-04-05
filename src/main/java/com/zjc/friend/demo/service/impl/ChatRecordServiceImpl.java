package com.zjc.friend.demo.service.impl;

import com.zjc.friend.demo.entity.ChatRecord;
import com.zjc.friend.demo.mapper.ChatRecordMapper;
import com.zjc.friend.demo.service.IChatRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 聊天记录表 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements IChatRecordService {

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Override
    public List<ChatRecord> queryChatInfo(ChatRecord chatRecord) {
        return chatRecordMapper.queryChatInfo(chatRecord);
    }
}
