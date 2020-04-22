package com.zjc.friend.demo.friend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.zjc.friend.demo.entity.FriendCategory;
import com.zjc.friend.demo.entity.FriendRequest;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IFriendCategoryService;
import com.zjc.friend.demo.friend.service.IFriendRequestService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.util.ResponseObject;

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
    /**
     * 好友分组服务类，包含对好友分组的增删改查
     */
    @Autowired
    private IFriendCategoryService friendCategoryService;

    /**
     * 好友请求服务类，包含对好友请求的增删改查
     */
    @Autowired
    private IFriendRequestService friendRequestService;

    /**
     * 用户信息服务类，包含对用户的增删改查
     */
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
        /**
         * 判断分组信息是否为空
         */
        if (!StringUtils.isNotBlank(categoryName)) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "");
        }
        // 获取当前登陆人信息
        Userinfo userInfo = currentUser.getUserInfo(userService);
        if (null == userInfo.getId()) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "");
        }
        // 创建好友分组对象
        FriendCategory category = new FriendCategory();
        // 设置分组创建时间
        category.setCreateTime(new Date());
        // 设置创建人id
        category.setUserId(userInfo.getId());
        // 设置分组名
        category.setCategoryName(categoryName);
        // 保存分组信息
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
        /**
         * 创建更新的分组对象
         */
        UpdateWrapper<FriendCategory> wrapper = new UpdateWrapper<>();
        // 设置更新时间为当前时间
        wrapper.set("update_time", new Date());
        // 设置分组名
        wrapper.set("category_name", name);
        // 设置更改分组id
        wrapper.eq("id", id);
        // 更新分组信息
        friendCategoryService.update(wrapper);
        // 提示成功
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
        // 根据分组id删除分组信息
        friendCategoryService.removeById(id);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "删除成功!");
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
        // 获取当前登陆用户信息
        Userinfo user = currentUser.getUserInfo(userService);
        // 根据当前分组id获取分类信息
        FriendCategory friendCategory = friendCategoryService.getById(catgoeyId);
        /**
         * 当前分组信息为空时，提示
         */
        if (null == friendCategory) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "系统错误");
        }
        // 创建更新分组对象
        UpdateWrapper<FriendRequest> wrapper = new UpdateWrapper<>();
        // 设置分组id
        wrapper.set("category_id", catgoeyId);
        // 设置好友id
        wrapper.eq("id", friendId);
        // 更新数据库
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
        // 获取当前登陆用户信息
        Userinfo userInfo = currentUser.getUserInfo(userService);
        // 创建查询条件对象
        QueryWrapper<FriendCategory> wrapper = new QueryWrapper<>();
        // 将当前用户id存入条件中
        wrapper.eq("user_id", userInfo.getId());
        // 根据当前用户id查询好友分组信息
        List<FriendCategory> list = friendCategoryService.list(wrapper);
        List<FriendCategory> categoryList = new ArrayList<>();
        /**
         * 判断查询的分组对象是否存在
         */
        for (int i = 0; i < list.size(); i++) {
            FriendCategory category = list.get(i);
            if (null != catId) {
                if (!category.getId().equals(catId)) {
                    categoryList.add(category);
                }
            }
        }
        // 如存在，提示
        if (categoryList.size() > 0) {
            return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功", categoryList);
        }
        System.out.println("categoryList:" + list);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功", list);
        // return new ResponseObject(StatusCode.FAILED.getCode(), "失败");
    }

    /**
     * 分组列表
     * @param model
     * @return
     */
    @RequestMapping("/queryCategoryInfo")
    public ModelAndView queryCategoryInfo(Model model) {
        // 获取当前登陆用户信息
        Userinfo userInfo = currentUser.getUserInfo(userService);
        // 设置当前用户id为查询条件
        QueryWrapper<FriendCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userInfo.getId());
        // 查询分组聊表
        List<FriendCategory> list = friendCategoryService.list(wrapper);
        // 返回信息到页面
        model.addAttribute("category", list);
        return new ModelAndView("/friend/html/friend", "info", list);
    }
}
