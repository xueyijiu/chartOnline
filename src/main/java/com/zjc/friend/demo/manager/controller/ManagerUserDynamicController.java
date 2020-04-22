package com.zjc.friend.demo.manager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.DynamicFile;
import com.zjc.friend.demo.entity.UserDynamic;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IDynamicFileService;
import com.zjc.friend.demo.friend.service.IUserDynamicService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.PageInfo;
import com.zjc.friend.demo.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户动态 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/manage/user-dynamic")
@RequiredPermission(PermissionConstants.ADMIN)
public class ManagerUserDynamicController {

    @Autowired
    private IUserDynamicService userDynamicService;

    @Autowired
    private IDynamicFileService dynamicFileService;

    @Autowired
    private IUserinfoService userService;

    @Value("${com.zjc.friend.image-path}")
    private String imagePath;

    @Value("${com.zjc.friend.web-root}")
    private String webRoot;


    @RequestMapping("/dynamicList")
    public Map<String, Object> dynamicList(@RequestParam(required = false) String username,@RequestParam(required = false) String dynaminContent, @RequestParam(required = false) Boolean status, @RequestParam int page, @RequestParam int limit) {
        QueryWrapper<UserDynamic> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time"); //按创建时间降序排列
        if (null != status) {
            wrapper.eq("delete_status", status);
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(limit);
        pageInfo.setPageIndex(page);
        List<Long> collect=new ArrayList<>();
        UserDynamic userDynamic=new UserDynamic();
        if(StringUtils.isNotBlank(username)){
//            Userinfo byUserName = userService.findByUserName(username);
            QueryWrapper<Userinfo> wrapper1=new QueryWrapper<>();
            wrapper1.like("username",username);
            List<Userinfo> list = userService.list(wrapper1);
           collect = list.stream().map(Userinfo::getId).collect(Collectors.toList());
            if(collect.size()<=0){
                collect.add(53226652332266532l);
            }
           userDynamic.setUserIds(collect);
        }

        if(StringUtils.isNotBlank(dynaminContent)){
            userDynamic.setDynaminContent(dynaminContent);
        }

        IPage<UserDynamicDto> userDynamicIPage = userDynamicService.queryDynamic(pageInfo, userDynamic);
        for (UserDynamicDto userDynamicDto : userDynamicIPage.getRecords()) {
            userDynamicDto.setUsername(userDynamicDto.getUserinfo().getUsername());
            for (DynamicFile dynamicFile : userDynamicDto.getDynamicFiles()) {
                dynamicFile.setFilePath( webRoot+ dynamicFile.getFilePath());
            }
            if(userDynamicDto.getDeleteStatus()==0){
                userDynamicDto.setDeleteStatusString("可见");
            }
            else{
                userDynamicDto.setDeleteStatusString("不可见");
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        data.put("count", userDynamicIPage.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        data.put("data", userDynamicIPage.getRecords());
        return data;
    }

    /***
     * 插找单条动态信息
     * @param id
     * @return
     */
    @RequestMapping("/queryADynamic")
    public UserDynamic queryADynamic(@RequestParam Long id) {
        UserDynamic byId = userDynamicService.getById(id);
        if (null != byId) {
            QueryWrapper<DynamicFile> wrapper = new QueryWrapper<>();
            wrapper.eq("dynamic_id", byId.getId());
            List<DynamicFile> list = dynamicFileService.list(wrapper);
        }
        return byId;
    }

    /**
     * 修改动态状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/updateDyStatus")
    public ResponseObject deleteDyStatus(@RequestParam Long id, @RequestParam Integer status){
        UpdateWrapper<UserDynamic> wrapper=new UpdateWrapper<>();
        wrapper.set("delete_status",status);
        wrapper.eq("id",id);
        boolean update = userDynamicService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"",update);
    }
}
