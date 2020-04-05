package com.zjc.friend.demo.service;

import com.zjc.friend.demo.entity.ChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 聊天记录表 服务类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface IChatRecordService extends IService<ChatRecord> {

    List<ChatRecord> queryChatInfo(ChatRecord chatRecord);
}
