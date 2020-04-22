package com.zjc.friend.demo.manager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.JsonData;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.Comment;
import com.zjc.friend.demo.entity.ControlTable;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.ICommentService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.manager.service.IControlTableService;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/manage/comment")
@RequiredPermission(PermissionConstants.ADMIN)
public class ManagerCommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private IControlTableService controlTableService;


    /**
     * 监控消息
     *
     * @param message
     * @return
     */
    @RequestMapping("/intercept")
    public ResponseObject intercept(String message) {
        List<ControlTable> controlTables = controlTableService.list();
        if (controlTables.size() > 0) {
            for (ControlTable table : controlTables) {
                if (table.getContent().contains(message)) {
                    return new ResponseObject(400, "你的言语不能通过该系统！");
                }
            }
        }
        return new ResponseObject(200, "成功");
    }

    /**
     * 查找评论列表
     *
     * @param page
     * @param limit
     * @param message
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/queryCommentList")
    public Map<String, Object> queryCommentList(@RequestParam Long page, @RequestParam Long limit, @RequestParam(required = false) String message,
                                                @RequestParam(required = false) Date startTime, @RequestParam(required = false) Date endTime,
                                                @RequestParam(required = false) String username) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(message)) {
            wrapper.like("comment_content", message);
        }
        if (null != startTime) {
            wrapper.gt("create_time", startTime);
        }
        if (null != endTime) {
            wrapper.lt("create_time", endTime);
        }
        if(StringUtils.isNotBlank(username)){
//            Userinfo byUserName = userService.findByUserName(username);
            QueryWrapper<Userinfo> wrapper1=new QueryWrapper<>();
            wrapper1.like("username",username);
            List<Userinfo> list = userService.list(wrapper1);
            List<Long> collect = list.stream().map(Userinfo::getId).collect(Collectors.toList());
            if(collect.size()<=0){
               collect.add(53226652332266532l);
            }
            wrapper.in("user_id",collect);
        }
        Page page2 = new Page();
        page2.setCurrent(page);
        page2.setSize(limit);
        IPage<Comment> page1 = commentService.page(page2, wrapper);
        List<Userinfo> allUser = userService.list();
        List<Comment> commentList=new ArrayList<>();
        for (Comment comment:page1.getRecords()) {
            if(allUser.size()>0)
            {
                for (Userinfo user:allUser) {
                    if(comment.getUserId().equals(user.getId())){
                        comment.setCommentUser(user.getUsername());
                    }
                }
                commentList.add(comment);
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        data.put("count", page1.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        data.put("data", commentList);
      return data;
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @RequestMapping("/deleteComment")
    public ResponseObject deleteComment(@RequestParam Long id){
        boolean b = commentService.removeById(id);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"",b);
    }

}
