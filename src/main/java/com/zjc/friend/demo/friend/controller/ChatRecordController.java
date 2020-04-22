package com.zjc.friend.demo.friend.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.ChatRecord;
import com.zjc.friend.demo.entity.InMessage;
import com.zjc.friend.demo.entity.MyPrincipal;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IChatRecordService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.friend.service.impl.WebSocketService;
import com.zjc.friend.demo.util.ResponseObject;

/**
 * <p>
 * 聊天记录表 前端控制器
 * </p>
 * @since 2020-02-25
 */
@RestController
@RequestMapping("/chat-record")
public class ChatRecordController {
    /**
     * WebSocket服务类，包含实时聊天的方法
     */
    @Autowired
    private WebSocketService ws;

    /**
     * 聊天记录的服务类，包含对聊天记录的增删改查
     */
    @Autowired
    private IChatRecordService chatRecordService;

    /**
     * 用户信息服务类，包含对用户的增删改查
     */
    @Autowired
    private IUserinfoService userinfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CurrentUser currentUser;

    /**
     * 添加聊天信息
     *
     * @param chatRecord
     * @return
     */
    @RequestMapping("/addChatInfo")
    @ResponseBody
    public ResponseObject addChatInfo(ChatRecord chatRecord) {
        // 调用getUserInfo获取当前登录人的信息
        Userinfo userInfo = currentUser.getUserInfo(userinfoService);
        // 将当前登陆用户id，状态 存入聊天记录chatRecord中
        chatRecord.setUserId(userInfo.getId());
        chatRecord.setCreateTime(new Date());
        chatRecord.setStatus(false);
        // 保存聊天记录
        chatRecordService.save(chatRecord);
        return null;
    }

    /**
     * 查找聊天信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/queryChatInfo")
    @ResponseBody
    public ModelAndView queryChatInfo(@RequestParam Long id, Model model) {
        // 获取登陆人的信息
        Userinfo userInfo = currentUser.getUserInfo(userinfoService);
        if (null == id) {
            return null;
        }
        // 通过好友id，聊天信息状态查询是否有聊天记录
        QueryWrapper<ChatRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("friend_id", id);
        wrapper.eq("status", false);
        // 查询聊天记录表还未查看的记录
        // select * from chat_rocord from feiend_id=#{userInfo.getId()} and status=false
        List<ChatRecord> list1 = chatRecordService.list(wrapper);
        for (int i = 0; i < list1.size(); i++) {
            ChatRecord record = list1.get(i);
            record.setStatus(true);
            // 更新聊天记录的状态为已读
            boolean b = chatRecordService.updateById(record);
            // 如果更新失败抛出异常
            if (!b) {
                throw new RuntimeException("状态改变失败");
            }
        }
        ChatRecord record = new ChatRecord();
        record.setUserId(id);
        record.setFriendId(userInfo.getId());
        // 查找聊天记录表的全部信息
        List<ChatRecord> list = chatRecordService.queryChatInfo(record);
        // 返回聊天信息到前端
        model.addAttribute("chat", list);
        // 返回用户信息
        model.addAttribute("user", userInfo);
        model.addAttribute("to", id);
        // 渲染chatRecord视图
        return new ModelAndView("/friend/html/chatRecord", "info", model);
    }

    /**
     * 更新消息状态
     *
     * @param id
     * @return
     */
    @RequestMapping("/updateChatInfo")
    public ResponseObject updateChatInfo(@RequestParam Long id, @RequestParam Boolean status) {
        UpdateWrapper<ChatRecord> wrapper = new UpdateWrapper<>();
        wrapper.set("status", status);
        wrapper.eq("id", id);
        // 更新聊天的状态
        chatRecordService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "更新成功");
    }

    /**
     * 聊天
     *
     * @param message
     */
    @MessageMapping("/v3/single/chat")
    public void singleChat(MyPrincipal principal, InMessage message) {
        // 查询对方用户信息
        Userinfo one = userinfoService.getById(message.getTo());
        // 设置对方用户名
        message.setTo(one.getUsername());
        // 存入发送的信息
        message.setFrom(String.valueOf(message.getFrom()));
        // 发送信息
        ws.sendChatMessage(message);

    }

}
