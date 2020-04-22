package com.zjc.friend.demo.friend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.ChatRecord;
import com.zjc.friend.demo.entity.FriendCategory;
import com.zjc.friend.demo.entity.FriendRequest;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IChatRecordService;
import com.zjc.friend.demo.friend.service.IFriendCategoryService;
import com.zjc.friend.demo.friend.service.IFriendRequestService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.util.ResponseObject;

/**
 * 好友请求
 */
@Controller
@RequestMapping("/friend-request")
public class FriendRequestController {
    /**
     * 好友请求服务类，包含对好友请求的增删改查
     */

    @Autowired
    private IFriendRequestService friendRequestService;

    /**
     * 好友分组服务类，包含对好友分组的增删改查
     */
    @Autowired
    private IFriendCategoryService friendCategoryService;

    /**
     * 用户信息服务类，包含对用户信息的增删改查
     */
    @Autowired
    private IUserinfoService userService;

    /**
     * 聊天记录服务类，包含对聊天记录的增删改查
     */
    @Autowired
    private IChatRecordService chatRecordService;

    @Autowired
    private CurrentUser currentUser;

    @Value("${com.zjc.friend.web-root}")
    private String webRoot;

    /**
     * 添加好友
     *
     * @param userId
     * @param catId
     * @param friendComment
     * @return
     */
    @RequestMapping("/insertFriend")
    @ResponseBody
    @Transactional
    public ResponseObject insertFriend(@RequestParam String userId, @RequestParam(required = false) Long catId, @RequestParam(required = false) String friendComment) {
        // 创建好友申请对象
        FriendRequest friendRequest = new FriendRequest();
        // 获取当前登陆用户信息
        Userinfo one = currentUser.getUserInfo(userService);
        /**
         * 获取当前登陆人的好友分组信息id
         */
        if (null == catId) {
            catId = getCategoryInfo(one);
        }
        // 设置分组id
        friendRequest.setCategoryId(catId);
        // 设置创建时间
        friendRequest.setCreateTime(new Date());
        // 设置请求状态0为为操作，1为同意，2为不同意
        friendRequest.setRequestStatus(0);
        friendRequest.setCode(true);
        if (StringUtils.isNotBlank(friendComment)) {
            // 设置好友标记
            friendRequest.setRemark(friendComment);
        }
        // 设置当前用户id
        friendRequest.setUserId(one.getId());
        // 设置好友id
        friendRequest.setFriendId(Long.valueOf(userId));
        // 保存好友请求信息，返回页面并提示
        boolean save = friendRequestService.save(friendRequest);
        if (!save) {
            throw new RuntimeException("添加失败");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "添加成功");
    }

    private Long getCategoryInfo(Userinfo one) {
        Long catId;
        QueryWrapper<FriendCategory> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", one.getId());
        List<FriendCategory> list = friendCategoryService.list(wrapper1);
        if (list.size() > 0) {
            catId = list.get(0).getId();
        } else {
            FriendCategory category = new FriendCategory();
            category.setCategoryName("我的好友!");
            category.setUserId(one.getId());
            category.setCreateTime(new Date());
            friendCategoryService.save(category);
            catId = category.getId();
        }
        return catId;
    }

    /**
     * 好友列表
     *
     * @param name
     * @return
     */
    @RequestMapping("/queryAllUser")
    public ModelAndView queryFriend(@RequestParam(required = false) String name, @RequestParam Long id, Model model) throws Exception {
        // 获取当前登陆用户信息
        Userinfo user = currentUser.getUserInfo(userService);
        // 创建查询条件
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            // 设置申请状态为同意
            wrapper.eq("status", true);
            // 根据用户名和电话模糊查询
            wrapper.and(queryWrapper -> queryWrapper.like("username", name).or().like("phone", name));
        }
        /**
         * 判断id是否与当前用户id是否匹配
         */
        if (null != id) {
            if (user.getId().equals(id)) {
                // 设置用户头像
                user.setUserPic(webRoot + user.getUserPic());
                model.addAttribute("info", user);
                return new ModelAndView("/friend/html/personal", "user", model);
            }
            wrapper.eq("id", id);
        }
        /**
         * 查询当前好友请求信息列表
         */
        Userinfo userinfo = userService.getById(id);
        QueryWrapper<FriendRequest> wrapper1 = new QueryWrapper<>();
        wrapper1.and(queryWrapper -> queryWrapper.eq("user_id", user.getId()).or().eq("friend_id", user.getId()));
        wrapper1.eq("request_status", 1);
        List<FriendRequest> friendRequestList = friendRequestService.list(wrapper1);
        userinfo.setUserPic(webRoot + userinfo.getUserPic());
        for (FriendRequest request : friendRequestList) {
            System.out.println(request.getUserId() + "==" + request.getFriendId());
            if (request.getFriendId().equals(id) || request.getUserId().equals(id)) {
                userinfo.setMyFriend(true);
                break;
            }
        }
        /**
         * 查询当前分组信息列表
         */
        QueryWrapper<FriendCategory> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("user_id", user.getId());
        List<FriendCategory> categoryList = friendCategoryService.list(categoryQueryWrapper);
        model.addAttribute("info", userinfo);
        model.addAttribute("category", categoryList);
        return new ModelAndView("/friend/html/friendAdd", "user", model);
    }

    /**
     * 查找自己好友信息
     *
     * @param name
     * @return
     */
    @RequestMapping("/queryMyUserFriend")
    public ModelAndView queryMyUserFriend(@RequestParam(required = false) String name, Model model) {
        // 获取当登陆用户信息
        Userinfo userInfo = currentUser.getUserInfo(userService);
        // 设置查询条件
        QueryWrapper<FriendRequest> wrapper = new QueryWrapper<>();
        wrapper.eq("status", false);
        // id条件为当前用户id
        wrapper.eq("user_id", userInfo.getId());
        // 状态为已同意
        wrapper.eq("request_status", 1);
        // 根据姓名模糊查询
        if (StringUtils.isNotBlank(name)) {
            wrapper.and(queryWrapper -> queryWrapper.like("username", name).or().like("phone", name));
        }
        List<FriendRequest> friendRequestList = friendRequestService.list(wrapper);
        /**
         * 根据用户id获取当前用户的好友分组信息
         */
        QueryWrapper<FriendCategory> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", userInfo.getId());
        List<FriendCategory> friendCategoryList = friendCategoryService.list(wrapper1);
        List<FriendCategory> categoryList = new ArrayList<>();
        for (int i = 0; i < friendCategoryList.size(); i++) {
            FriendCategory category = friendCategoryList.get(i);
            List<FriendRequest> friendRequests = new ArrayList<>();
            for (int j = 0; j < friendRequestList.size(); j++) {
                FriendRequest friendRequest = friendRequestList.get(j);
                /**
                 * 判断当前好友分组正确
                 */
                if (category.getId().equals(friendRequest.getCategoryId())) {
                    // 查询好友信息
                    Userinfo byId = userService.getById(friendRequest.getFriendId());
                    if (null != byId.getUserPic()) {
                        // 存入好友信息头像
                        byId.setUserPic(webRoot + byId.getUserPic());
                    }
                    // 查询好友聊天记录信息
                    QueryWrapper<ChatRecord> wrapper2 = new QueryWrapper<>();
                    // 条件为好友id
                    wrapper2.eq("friend_id", userInfo.getId());
                    // 状态为未读
                    wrapper2.eq("status", false);
                    // 判断当前好友id是否等于自身id
                    if (friendRequest.getFriendId().equals(userInfo.getId())) {

                        wrapper2.eq("user_id", friendRequest.getUserId());
                    } else {
                        wrapper2.eq("user_id", friendRequest.getFriendId());
                    }
                    /**
                     * 将聊天信息存入聊天记录集合汇总
                     */
                    List<ChatRecord> list = chatRecordService.list(wrapper2);
                    friendRequest.setMessageCount(list.size());
                    friendRequest.setUserinfo(byId);
                    friendRequests.add(friendRequest);
                }
            }
            /**
             * 返回当前用户的好友请求信息
             */
            category.setFriendRequestList(friendRequests);
            categoryList.add(category);
        }
        int all = 0;
        ResponseObject responseObject = queryApplyFriend(all);
        if (responseObject.getStatus() == 200) {
            model.addAttribute("request", responseObject.getData());
        }
        System.out.println(categoryList);
        model.addAttribute("category", categoryList);
        model.addAttribute("all", all);// 查看好友请求个数
        return new ModelAndView("/friend/html/friend", "info", model);
    }

    /**
     * 查找好友请求
     *
     * @return
     */
    @RequestMapping("/queryApplyFriend")
    public ResponseObject queryApplyFriend(int all) {
        Userinfo one = currentUser.getUserInfo(userService);
        QueryWrapper<FriendRequest> wrapper = new QueryWrapper<>();
        wrapper.and(queryWrapper -> queryWrapper.eq("user_id", one.getId()).or().eq("friend_id", one.getId()));
        wrapper.orderByDesc("create_time");
        wrapper.eq("status", 0);
        wrapper.eq("code", true);
        List<FriendRequest> list = friendRequestService.list(wrapper);
        List<FriendRequest> friendRequestList = new ArrayList<>();
        Userinfo byId = null;
        for (FriendRequest request : list) {
            if (one.getId().equals(request.getUserId())) {
                byId = userService.getById(request.getFriendId());
            } else {
                byId = userService.getById(request.getUserId());
            }
            if (request.getStatus() == 0) {
                all++;
            }
            if (null != byId.getUserPic()) {
                byId.setUserPic(webRoot + byId.getUserPic());
            }
            request.setUserinfo(byId);
            friendRequestList.add(request);
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "", friendRequestList);
    }

    /**
     * 更新好友申请
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/updateApplyFriend")
    @ResponseBody
    @Transactional
    public ResponseObject updateApplyFriend(@RequestParam Long id, @RequestParam Integer status, @RequestParam(required = false) Long catId, @RequestParam(required = false) String commentName) {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        FriendRequest byId = friendRequestService.getById(id);
        System.out.println(byId);
        UpdateWrapper<FriendRequest> wrapper = new UpdateWrapper<>();
        wrapper.set("request_status", status);
        wrapper.eq("id", id);
        wrapper.set("status", 0);
        catId = getCatId(id, status, catId, userInfo.getId()); // 判断是否有分组
        System.out.println("catId:" + catId);
        // wrapper.set("category_id", catId);
        friendRequestService.update(wrapper);
        if (status == 1) {
            FriendRequest request = new FriendRequest();
            request.setRequestStatus(1);
            request.setUserId(userInfo.getId());
            catId = getCatId(id, status, catId, userInfo.getId()); // 判断是否有分组
            System.out.println("catId1:" + catId);
            request.setCategoryId(catId);
            request.setUserId(byId.getFriendId());
            request.setFriendId(byId.getUserId());
            request.setCreateTime(new Date());
            request.setRemark(commentName);
            request.setCode(false);
            friendRequestService.save(request);
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功!");
    }

    private Long getCatId(Long id, Integer status, @RequestParam(required = false) Long catId, Long userId) {
        if (1 == status) {
            QueryWrapper<FriendCategory> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.eq("user_id", userId);
            List<FriendCategory> list = friendCategoryService.list(categoryQueryWrapper);
            if (list.size() > 0) {
                if (null == catId) {
                    catId = list.get(0).getId();
                }
            } else {
                FriendCategory category = new FriendCategory();
                category.setCreateTime(new Date());
                category.setUserId(userId);
                category.setCategoryName("我的好友!");
                friendCategoryService.save(category);
                catId = category.getId();
            }
        } else {
            return 0L;
        }
        System.out.println("catId2:" + catId);
        return catId;
    }

    /**
     * 删除好友信息(逻辑删除)
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFriend")
    @ResponseBody
    public ResponseObject deleteFriend(@RequestParam Long id) {
        UpdateWrapper<FriendRequest> wrapper = new UpdateWrapper<>();
        wrapper.set("status", 1);
        wrapper.eq("id", id);
        boolean update = friendRequestService.update(wrapper);
        if (!update) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "删除失败!");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "删除成功!");
    }

}
