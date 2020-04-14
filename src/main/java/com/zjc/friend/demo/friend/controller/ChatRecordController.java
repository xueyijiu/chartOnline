package com.zjc.friend.demo.friend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.ChatRecord;
import com.zjc.friend.demo.entity.InMessage;
import com.zjc.friend.demo.entity.MyPrincipal;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IChatRecordService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.friend.service.impl.WebSocketService;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
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
        //调用getUserInfo获取当前登录人的信息
        Userinfo userInfo = currentUser.getUserInfo(userinfoService);
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
        //获取登陆人的信息
        Userinfo userInfo = currentUser.getUserInfo(userinfoService);
        if (null == id) {
            return null;
        }
        QueryWrapper<ChatRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("friend_id", id);
        wrapper.eq("status", false);
        //查询聊天记录表还未查看的记录
        //select * from chat_rocord from feiend_id=#{userInfo.getId()} and status=false
        List<ChatRecord> list1 = chatRecordService.list(wrapper);
        for (int i = 0; i < list1.size(); i++) {
            ChatRecord record = list1.get(i);
            record.setStatus(true);
            //更新聊天记录的状态
            boolean b = chatRecordService.updateById(record);
            //如果更新失败抛出异常
            if (!b) {
                throw new RuntimeException("状态改变失败");
            }
        }
        ChatRecord record = new ChatRecord();
        record.setUserId(id);
        record.setFriendId(userInfo.getId());
        //查找聊天记录表的全部信息
        List<ChatRecord> list = chatRecordService.queryChatInfo(record);
        //返回聊天信息到前端
        model.addAttribute("chat", list);
        //返回用户信息
        model.addAttribute("user", userInfo);
        model.addAttribute("to",id);
        //渲染chatRecord视图
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
        //更新聊天的状态
        chatRecordService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "更新成功");
    }


    /**
     * 单人聊天
     *
     * @param message
     */
    @MessageMapping("/v3/single/chat")
    public void singleChat(MyPrincipal principal,InMessage message) {
        Userinfo one = userinfoService.getById(message.getTo());
        message.setTo(one.getUsername());
        message.setFrom(String.valueOf(message.getFrom()));
        //发送信息
        ws.sendChatMessage(message);

    }

}
