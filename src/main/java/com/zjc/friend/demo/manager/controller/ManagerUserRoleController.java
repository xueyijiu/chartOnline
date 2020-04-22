package com.zjc.friend.demo.manager.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.JsonData;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.SysUserRole;
import com.zjc.friend.demo.friend.service.ISysUserRoleService;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by linziyu on 2019/2/12.
 * <p>
 * 用户角色处理视图
 */

@Controller
@Slf4j
@RequestMapping("/manage")
@RequiredPermission(PermissionConstants.ADMIN)
public class ManagerUserRoleController {

    @Autowired
    private ISysUserRoleService userRoleService;

    //设置为管理员
    @RequestMapping(value = "/setUserAdmin")
    @ResponseBody
    public JsonData changeUserRole(Long id) {
//        userRoleService.setUserAdmin(id);
        return new JsonData(200, "ok");

    }

    //设置为普通用户
    @RequestMapping(value = "/setUser")
    @ResponseBody
    public ResponseObject setUser(Long id) {
        UpdateWrapper<SysUserRole> wrapper=new UpdateWrapper<>();
        wrapper.set("role_id",2);
        wrapper.eq("user_id",id);
        userRoleService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"修改成功");
    }



}
