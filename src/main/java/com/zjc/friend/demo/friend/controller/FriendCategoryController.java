package com.zjc.friend.demo.friend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.FriendCategory;
import com.zjc.friend.demo.entity.FriendRequest;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IFriendCategoryService;
import com.zjc.friend.demo.friend.service.IFriendRequestService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 好友分组信息 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-02-26
 */
@RestController
@RequestMapping("/friend-category")
public class FriendCategoryController {

    @Autowired
    private IFriendCategoryService friendCategoryService;

    @Autowired
    private IFriendRequestService friendRequestService;

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private CurrentUser currentUser;

    /**
     * 增加分组
     *
     * @param categoryName
     * @return
     */
    @RequestMapping("/insertCategory")
    public ResponseObject insertCategory(@RequestParam String categoryName) {
        if (!StringUtils.isNotBlank(categoryName)) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "");
        }
        Userinfo userInfo = currentUser.getUserInfo(userService);
        if (null == userInfo.getId()) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "");
        }
        FriendCategory category = new FriendCategory();
        category.setCreateTime(new Date());
        category.setUserId(userInfo.getId());
        category.setCategoryName(categoryName);
        boolean save = friendCategoryService.save(category);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "添加成功!", category);
    }

    /**
     * 更新分组
     *
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/updateCategory")
    public ResponseObject updateCategory(@RequestParam Long id, @RequestParam String name) {
        FriendCategory category = new FriendCategory();
        UpdateWrapper<FriendCategory> wrapper = new UpdateWrapper<>();
        wrapper.set("update_time", new Date());
        wrapper.set("category_name", name);
        wrapper.eq("id", id);
        friendCategoryService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "");
    }

    /**
     * 删除分组
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteCatgory")
    public ResponseObject deleteCatgory(Long id) {
        friendCategoryService.removeById(id);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"删除成功!");
    }

    /**
     * 移动分组
     *
     * @param friendId
     * @param catgoeyId
     * @return
     */
    @RequestMapping("/moveCatgoey")
    @ResponseBody
    @Transactional
    public ResponseObject moveCatgoey(@RequestParam Long friendId, @RequestParam Long catgoeyId) {
        Userinfo user = currentUser.getUserInfo(userService);
        FriendRequest friendRequest = friendRequestService.getById(friendId);
        FriendCategory friendCategory = friendCategoryService.getById(catgoeyId);
        if (null == friendCategory) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "系统错误");
        }
        UpdateWrapper<FriendRequest> wrapper=new UpdateWrapper<>();
        wrapper.set("category_id",catgoeyId);
        wrapper.eq("id",friendId);
        friendRequestService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功");
    }

    /**
     * 查找分组信息
     *
     * @param
     * @param catId
     * @return
     */
    @RequestMapping("/queryCateoryList")
    @ResponseBody
    public ResponseObject queryCatoryList(@RequestParam(required = false) Long catId) {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        QueryWrapper<FriendCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userInfo.getId());
        List<FriendCategory> list = friendCategoryService.list(wrapper);
        List<FriendCategory> categoryList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            FriendCategory category = list.get(i);
            if(null!=catId){
                if (!category.getId().equals(catId)) {
                    categoryList.add(category);
                }
            }
        }
        if(categoryList.size()>0){
            return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功", categoryList);
        }
        System.out.println("categoryList:"+list);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功", list);
//        return new ResponseObject(StatusCode.FAILED.getCode(), "失败");
    }

    /**
     * 查找分组信息
     * @param model
     * @return
     */
    @RequestMapping("/queryCategoryInfo")
    public ModelAndView queryCategoryInfo(Model model) {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        QueryWrapper<FriendCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userInfo.getId());
        List<FriendCategory> list = friendCategoryService.list(wrapper);
        model.addAttribute("category", list);
        return new ModelAndView("/friend/html/friend", "info", list);
    }
}
