package com.zjc.friend.demo.mapper;

import com.zjc.friend.demo.entity.ChatRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 聊天记录表 Mapper 接口
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {

    List<ChatRecord> queryChatInfo(ChatRecord chatRecord);
}
