package com.zjc.friend.demo.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.JsonData;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.SysRole;
import com.zjc.friend.demo.entity.SysUserRole;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.ISysUserRoleService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.PageInfo;
import com.zjc.friend.demo.util.PasswordEncrypt;
import com.zjc.friend.demo.util.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linziyu on 2019/2/13.
 * <p>
 * 用户处理视图
 */

@Controller
@Slf4j
@RequestMapping("/manage")
@RequiredPermission(PermissionConstants.ADMIN)
public class ManagerUserController {

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @RequestMapping(value = "/deleteUserById")
    @ResponseBody
    public JsonData deleteUserById(Long id, String role) {
        if (role.equals("ROLE_ADMIN")) {
            return new JsonData(501, "NO");
        }
        UpdateWrapper<Userinfo> wrapper = new UpdateWrapper<>();
        wrapper.set("status", false);
        wrapper.eq("id", id);
        userService.update(wrapper);
        return new JsonData(200, "OK");
    }


//    @RequestMapping("/login_page")
//    public String login() {
//        return "/login";
//    }

    //用户资料分页
    @RequestMapping(value = "/getAllUser")
    @ResponseBody
    public Map<String, Object> page(int page, int limit) {
        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
//        log.info("{}","page");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageIndex(page);
        pageInfo.setPageSize(limit);
        System.out.println(page+"=="+limit);
        IPage<Userinfo> allUsers = userService.getAllUsers(pageInfo);
        for (Userinfo user : allUsers.getRecords()) {
            if(user.getRoles().size()>0) {
                user.setRole(user.getRoles().get(0).getName());//用户角色包装 方便处理
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        data.put("count", allUsers.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        System.out.println(allUsers.getRecords());
        data.put("data", allUsers.getRecords());
        //返回给前端
        return data;

    }

    /**
     * 展示用户信息
     *
     * @return
     */
    @RequestMapping("/display_user_info")
    public String displayUserInfo() {
        return "/manage/displayuserinfo";
    }

    /**
     * 修改密码
     *
     * @param name
     * @param pwd
     * @return
     */
    @RequestMapping("/updatePassword")
    @ResponseBody
    public ResponseObject updatePassword(@RequestParam String name, @RequestParam String pwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Userinfo byUserName = userService.findByUserName(name);
        if (null == byUserName) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "该用户不存在");
        }
        for (SysRole role : byUserName.getRoles()) {
            if (!role.getName().equals("ROLE_ADMIN")) {
                return new ResponseObject(StatusCode.FAILED.getCode(), "你不是管理员！");
            }
        }
        if (PasswordEncrypt.encodeByMd5(pwd).equals(byUserName.getPassword())) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "和上次密码一致");
        }
        String encode = PasswordEncrypt.encodeByMd5(pwd);
        if (!byUserName.getStatus()) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "该用户不存在");
        }
        UpdateWrapper<Userinfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", byUserName.getId());
        wrapper.set("password", encode);
        userService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功");
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseObject login(@RequestParam String username, @RequestParam String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        System.out.println(username + "==" + password);
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        /**
         * select * from userinfo u where (u.username=#{username} or u.phone=#{phone}) and password=#{password}
         */
        wrapper.and(queryWrapper -> queryWrapper.eq("username", username).or().eq("phone", username));
        wrapper.eq("password", PasswordEncrypt.encodeByMd5(password));
        Userinfo userinfo = userService.getOne(wrapper);
        if (null == userinfo) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "登陆失败");
        }
        QueryWrapper<SysUserRole> wrapper1=new QueryWrapper<>();
        wrapper1.eq("user_id",userinfo.getId());
        SysUserRole one = sysUserRoleService.getOne(wrapper1);
        if(one.getRoleId().equals(2)){
            return new ResponseObject(StatusCode.FAILED.getCode(), "登陆失败,你不是管理员!", userinfo);
        }
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("username",username);
        session.setAttribute("role",one.getRoleId());
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "登陆成功", userinfo);
    }

    /**
     * 退出
     * @param session
     * @param sessionStatus
     * @return
     */
    @RequestMapping("/logout")
    public ResponseObject logout(HttpSession session, SessionStatus sessionStatus) {
        session.removeAttribute("username");
        session.removeAttribute("role");
        sessionStatus.setComplete();
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"");
    }
}
