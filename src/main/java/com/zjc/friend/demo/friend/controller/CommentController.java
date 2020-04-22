package com.zjc.friend.demo.friend.controller;

import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.Comment;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.ICommentService;
import com.zjc.friend.demo.friend.service.IDynamicFileService;
import com.zjc.friend.demo.friend.service.IUserDynamicService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.util.ResponseObject;

/**
 *评论功能
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    /**
     * 用户信息服务类，包含对用户的增删改查
     */
    @Autowired
    private IUserinfoService userService;

    /**
     * 评论信息服务类，包含对评论的增删改查
     */
    @Autowired
    private ICommentService commentService;

    /**
     * 用户动态服务类，包含对用户动态的增删改查
     */
    @Autowired
    private IUserDynamicService userDynamicService;

    /**
     * 动态文件服务类，包含对动态文件的增删改查
     */
    @Autowired
    private IDynamicFileService dynamicFileService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrentUser currentUser;

    @Value("${com.zjc.friend.web-root1}")
    private String webRoot;

    /**
     * 新增评论
     *
     * @param id
     * @param comment
     * @return
     */
    @RequestMapping("/addComment")
    @ResponseBody
    public ResponseObject addComment(@RequestParam Long id, @RequestParam String comment, @RequestParam(required = false) Long user_id) throws Exception {
        // 判断评论信息是否为空
        if (StringUtils.isBlank(comment)) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "评论内容不能为空!");
        }
        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity("http://localhost:8081/manage/comment/intercept?message=" + comment, JSONObject.class);
        if (forEntity.getStatusCode().value() != 200) {
            return new ResponseObject(StatusCode.FAILED.getCode(), forEntity.getBody().toString());
        }
        // 创建评论对象
        Comment comment1 = new Comment();
        // 设置动态id
        comment1.setDynamicId(id);
        // 设置动态内容
        comment1.setCommentContent(comment);
        // 设置评论时间
        comment1.setCreateTime(new Date());
        // 获取当前用户信息
        Userinfo user = currentUser.getUserInfo(userService);
        // 设置评论人id
        comment1.setUserId(user.getId());
        // 保存评论
        boolean save = commentService.save(comment1);
        if (!save) {
            throw new Exception("提交错误");
        }
        /**
         * 将评论人信息存入评论对象中，并返回页面
         */
        comment1.setPetSrc(webRoot+user.getUserPic());
        comment1.setMyselfComment(true);
        comment1.setName(user.getUsername());
        comment1.setTimeString(new DateFormatter("yyyy/MM/dd HH:mm:ss").print(comment1.getCreateTime(), Locale.CHINA));
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "添加成功", comment1);
    }

    /**
     * 删除评论信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public ResponseObject deleteComment(@RequestParam Long id) {
        // 根据评论id删除评论信息
        boolean b = commentService.removeById(id);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "删除成功!");
    }

}
