package com.zjc.friend.demo.manager.controller;

import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.JsonData;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.security.RequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by linziyu on 2019/2/13.
 * <p>
 * 校验处理视图
 */

@Controller
@Slf4j
@RequestMapping("/manage")
@RequiredPermission(PermissionConstants.ADMIN)
public class ManagerValidationController {

    @Autowired
    private IUserinfoService userService;


    @RequestMapping(value = "/checkNameIsExistOrNot")
    @ResponseBody
    public JsonData checkUserNameISExistOrNot(String username) {
        Userinfo user = userService.findByUserName(username);
//        log.info("{}",user+"******");
        if (user != null) {
            return new JsonData(301, "用户名被占有了");

        } else {
            return new JsonData(200, "用户名可以使用");
        }
    }


}
