package com.zjc.friend.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.ChatRecord;
import com.zjc.friend.demo.entity.InMessage;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.service.IChatRecordService;
import com.zjc.friend.demo.service.IUserinfoService;
import com.zjc.friend.demo.service.impl.WebSocketService;
import com.zjc.friend.demo.util.CurrentUser;
import com.zjc.friend.demo.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 聊天记录表 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@RestController
@RequestMapping("/chat-record")
public class ChatRecordController {

    @Autowired
    private WebSocketService ws;

    @Autowired
    private IChatRecordService chatRecordService;

    @Autowired
    private IUserinfoService userinfoService;

    /**
     * 添加聊天信息
     *
     * @param chatRecord
     * @return
     */
    @RequestMapping("/addChatInfo")
    @ResponseBody
    public ResponseObject addChatInfo(ChatRecord chatRecord) {
        Userinfo userInfo = CurrentUser.getUserInfo(userinfoService);
        chatRecord.setUserId(userInfo.getId());
        chatRecord.setCreateTime(new Date());
        chatRecord.setStatus(false);
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
        Userinfo userInfo = CurrentUser.getUserInfo(userinfoService);
        if (null == id) {
            return null;
        }
        QueryWrapper<ChatRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("friend_id", userInfo.getId());
        wrapper.eq("status", false);
        List<ChatRecord> list1 = chatRecordService.list(wrapper);
        for (int i = 0; i < list1.size(); i++) {
            ChatRecord record = list1.get(i);
            record.setStatus(true);
            boolean b = chatRecordService.updateById(record);
            if (!b) {
                throw new RuntimeException("状态改变失败");
            }
        }
        ChatRecord record = new ChatRecord();
        record.setUserId(id);
        record.setFriendId(userInfo.getId());
        List<ChatRecord> list = chatRecordService.queryChatInfo(record);
        model.addAttribute("chat", list);
        model.addAttribute("user", userInfo);
        return new ModelAndView("/html/chatRecord", "info", model);
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
        chatRecordService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "更新成功");
    }


    /**
     * 单人聊天
     *
     * @param message
     */
    @MessageMapping("/v3/single/chat")
    public void singleChat(Principal principal, InMessage message) {
        System.out.println(principal.getName());
//        Userinfo userInfo = CurrentUser.getUserInfo(userinfoService);
//        QueryWrapper<Userinfo> wrapper=new QueryWrapper<>();
//        wrapper.eq("username",principal.getName());
//        System.out.println(userinfoService.list());
        Userinfo one = userinfoService.getById(message.getTo());
        message.setTo(one.getUsername());
        message.setFrom(String.valueOf(principal.getName()));
        ws.sendChatMessage(message);

    }

}
