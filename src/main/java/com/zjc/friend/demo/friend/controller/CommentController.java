package com.zjc.friend.demo.friend.controller;


import com.alibaba.fastjson.JSONObject;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.Comment;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.ICommentService;
import com.zjc.friend.demo.friend.service.IDynamicFileService;
import com.zjc.friend.demo.friend.service.IUserDynamicService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.ResponseObject;
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

import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserDynamicService userDynamicService;

    @Autowired
    private IDynamicFileService dynamicFileService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrentUser currentUser;




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
        System.out.println(id + "==" + comment);
        if (StringUtils.isBlank(comment)) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "评论内容不能为空!");
        }
//        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity(serviceUrl + "/comment/intercept?message=" + comment, JSONObject.class);
//        if (forEntity.getStatusCode().value() != 200) {
//            return new ResponseObject(StatusCode.FAILED.getCode(), forEntity.getBody().toString());
//        }
        Comment comment1 = new Comment();
        comment1.setDynamicId(id);
        comment1.setCommentContent(comment);
        comment1.setCreateTime(new Date());

        Userinfo user = currentUser.getUserInfo(userService);
        comment1.setUserId(user.getId());
        boolean save = commentService.save(comment1);
        if (!save) {
            throw new Exception("提交错误");
        }
        comment1.setPetSrc(user.getUserPic());
        comment1.setMyselfComment(true);
        comment1.setName(user.getUsername());
        comment1.setTimeString(new DateFormatter("yyyy/MM/dd HH:mm:ss").print(comment1.getCreateTime(), Locale.CHINA));
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "添加成功", comment1);
    }

    /**
     * 查找评论
     * @param id
     * @return
     */
//    @RequestMapping("/queryComment")
//    public ResponseObject queryComment(@RequestParam Long id){
//        userDynamicService
//        return null;
//    }

    /**
     * 删除评论信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public ResponseObject deleteComment(@RequestParam Long id) {
        boolean b = commentService.removeById(id);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "删除成功!");
    }


}
