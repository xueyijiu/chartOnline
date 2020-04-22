package com.zjc.friend.demo.friend.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.common.FileOperator;
import com.zjc.friend.demo.common.FileResult;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.*;
import com.zjc.friend.demo.friend.service.IDynamicFileService;
import com.zjc.friend.demo.friend.service.IFriendRequestService;
import com.zjc.friend.demo.friend.service.IUserDynamicService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.PageInfo;
import com.zjc.friend.demo.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 用户动态 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Controller
@RequestMapping("/user-dynamic")
public class UserDynamicController {

    @Autowired
    private IUserDynamicService userDynamicService;

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private IDynamicFileService dynamicFileService;

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private IFriendRequestService friendRequestService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrentUser currentUser;

    @Value("${com.zjc.friend.web-root1}")
    private String webRoot;


    /**
     * 添加动态
     *
     * @param dynamic
     * @param files
     * @return
     */
    @RequestMapping("/insertDynamic")
    @ResponseBody
    @Transactional
    public ResponseObject insertDynamic(UserDynamic dynamic, MultipartFile files) throws IOException {

        if (StringUtils.isBlank(dynamic.getDynaminContent())) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "动态内容不能为空!");
        }
        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity("localhost:8081/manage/comment/intercept?message=" + dynamic.getDynaminContent(), JSONObject.class);
        if (forEntity.getStatusCode().value() != 200) {
            return new ResponseObject(StatusCode.FAILED.getCode(), forEntity.getBody().toString());
        }
        Userinfo userInfo = currentUser.getUserInfo(userService);
        dynamic.setCreateTime(new Date());
        dynamic.setDeleteStatus(0);
        dynamic.setUserId(userInfo.getId());
        userDynamicService.save(dynamic);
        FileResult fileResult = null;
        System.out.println(files);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(files.getOriginalFilename())) {
            fileResult = fileOperator.uploadFile(files.getInputStream(), files.getOriginalFilename());
        }
//            for (String key:map.keySet()) {
        DynamicFile dynamicFile = new DynamicFile();
        if (null != fileResult) {
            dynamicFile.setFilePath(fileResult.getRelativePath());
        }
        dynamicFile.setImageName(files.getOriginalFilename());
        dynamicFile.setDynamicId(dynamic.getId());
        if (!dynamicFileService.save(dynamicFile)) {
            throw new RuntimeException("图片添加失败");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "动态添加失败");
    }

    /**
     * 更新动态信息
     *
     * @return
     */
    @RequestMapping("/updateDynamic")
    @ResponseBody
    @Transactional
    public ResponseObject updateDynamic(UserDynamic dynamic, MultipartFile files) throws IOException {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        dynamic.setUserId(userInfo.getId());
        dynamic.setDeleteStatus(0);
        dynamic.setCreateTime(new Date());
        userDynamicService.updateById(dynamic);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(files.getOriginalFilename())) {
            FileResult fileResult = fileOperator.uploadFile(files.getInputStream(), files.getOriginalFilename());
            QueryWrapper<DynamicFile> wrapper = new QueryWrapper<>();
            wrapper.eq("dynamic_id", dynamic.getId());
            dynamicFileService.remove(wrapper);
            DynamicFile dynamicFile = new DynamicFile();
            if (null != fileResult) {
                dynamicFile.setFilePath(fileResult.getRelativePath());
                dynamicFile.setImageName(files.getOriginalFilename());
                dynamicFile.setDynamicId(dynamic.getId());
                if (!dynamicFileService.save(dynamicFile)) {
                    throw new RuntimeException("图片添加失败");
                }
            }
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "动态添加成功");
    }

    /**
     * 删除动态（逻辑删除）
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteDynamicFile")
    @Transactional
    @ResponseBody
    public ResponseObject deleteDynamicFile(@RequestParam Long id) {
        UpdateWrapper<UserDynamic> userDynamicUpdateWrapper = new UpdateWrapper<>();
        userDynamicUpdateWrapper.set("delete_status", 1);
        userDynamicUpdateWrapper.eq("id", id);
        boolean update = userDynamicService.update(userDynamicUpdateWrapper);
        if (!update) {
            throw new RuntimeException("删除失败!");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "删除成功");
    }

    /**
     * 查找动态
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectDynamic")
    public ModelAndView selectDynamic(@RequestParam Integer pageIndex, @RequestParam Integer pageSize, Model model) {
        Page page=new Page();
        page.setCurrent(pageIndex);
        page.setSize(pageSize);
        Userinfo userInfo = currentUser.getUserInfo(userService);
        IPage<UserDynamicDto> userDynamicDtoIPage = userDynamicService.queryDynamicInfo(page,userInfo.getId());
        List<UserDynamicDto> userDynamicDtoList = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        for (UserDynamicDto dynamicDto : userDynamicDtoIPage.getRecords()) {
            List<DynamicFile> dynamicFileList = new ArrayList<>();
            if (dynamicDto.getUserId().equals(userInfo.getId())) {
                dynamicDto.setIsNotMyself(true);
            }
            if (null != dynamicDto.getDynamicFiles() && dynamicDto.getDynamicFiles().size() > 0) {
                for (int i = 0; i < dynamicDto.getDynamicFiles().size(); i++) {
                    DynamicFile dynamicFile = dynamicDto.getDynamicFiles().get(i);
                    dynamicFile.setFilePath(webRoot + dynamicFile.getFilePath());
                    dynamicFileList.add(dynamicFile);
                }
            }
            if (null != dynamicDto.getCommentList()) {
                for (Comment comment : dynamicDto.getCommentList()) {
                    Userinfo byId = userService.getById(comment.getUserId());
                    if (comment.getUserId().equals(userInfo.getId())) {
                        comment.setMyselfComment(true);
                    }
                    if (StringUtils.isNotBlank(byId.getUserPic())) {
                        comment.setPetSrc(webRoot+byId.getUserPic());
                    }
                    if (null != (byId.getUsername())) {
                        comment.setName(byId.getUsername());
                    }
                    System.out.println("comment:"+comment);
                    commentList.add(comment);
                }
            }
            if(null!=dynamicDto.getUserinfo()){
                if(StringUtils.isNotBlank(dynamicDto.getUserinfo().getUserPic())){
                    dynamicDto.getUserinfo().setUserPic(webRoot+dynamicDto.getUserinfo().getUserPic());
                }
            }
            dynamicDto.setDynamicFiles(dynamicFileList);
            dynamicDto.setCommentList(commentList);
            userDynamicDtoList.add(dynamicDto);
        }

        model.addAttribute("page",page);
        model.addAttribute("info", userDynamicDtoList);
        return new ModelAndView("friend/html/Mydynamic", "userInfo", model);
    }

    /**
     * 查找全部动态
     *
     * @return
     */
    @RequestMapping("/allDynamicInfo")
    public ModelAndView allDynamicInfo(Model model,@RequestParam Integer pageIndex,@RequestParam Integer pageSize) {
        Long lds = null;
        Page page=new Page();
        page.setCurrent(pageIndex);
        page.setSize(pageSize);
        IPage<UserDynamicDto> userDynamicDtoIPage = userDynamicService.queryDynamicInfo(page,lds);
        Userinfo user = currentUser.getUserInfo(userService);
        List<UserDynamicDto> userDynamicList = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        PageInfo pageInfo=new PageInfo();
        for (int i = 0; i < userDynamicDtoIPage.getRecords().size(); i++) {
            UserDynamicDto dynamicDto = userDynamicDtoIPage.getRecords().get(i);
            List<DynamicFile> dynamicFileList = new ArrayList<>();
            if(dynamicDto!=null && null!=dynamicDto.getCommentList()) {
                for (Comment comment : dynamicDto.getCommentList()) {
                    System.out.println("user:"+comment);
                    Userinfo byId = userService.getById(comment.getUserId());
                    if (null != user) {
                        if (comment.getUserId().equals(user.getId())) {
                            comment.setMyselfComment(true);
                        }
                    }
                    if (null != byId.getUserPic()) {
                        comment.setPetSrc(webRoot + byId.getUserPic());
                    }
                    if (null != (byId.getUsername())) {
                        comment.setName(byId.getUsername());
                    }
                    commentList.add(comment);
                }
            }
            if (dynamicDto!=null && null != dynamicDto.getDynamicFiles() && dynamicDto.getDynamicFiles().size() > 0) {
                for (int j = 0; j < dynamicDto.getDynamicFiles().size(); j++) {
                    DynamicFile dynamicFile = dynamicDto.getDynamicFiles().get(j);
                    dynamicFile.setFilePath(webRoot + dynamicFile.getFilePath());
                    dynamicFileList.add(dynamicFile);
                }
            }
            if(null!=dynamicDto && null!=dynamicDto.getUserinfo()){
                if(StringUtils.isNotBlank(dynamicDto.getUserinfo().getUserPic())){
                    dynamicDto.getUserinfo().setUserPic(webRoot+dynamicDto.getUserinfo().getUserPic());
                }
            }

            dynamicDto.setCommentList(commentList);
            dynamicDto.setDynamicFiles(dynamicFileList);
            userDynamicList.add(dynamicDto);
        }
        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageIndex(page.getCurrent());
        pageInfo.setTotalCount(page.getPages());
        model.addAttribute("info", userDynamicList);
        model.addAttribute("page",pageInfo);
//        model.addAttribute("page",pageInfo);

        return new ModelAndView("friend/html/dynamic", "userInfo", model);
    }

}
