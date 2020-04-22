package com.zjc.friend.demo.friend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjc.friend.demo.common.FileOperator;
import com.zjc.friend.demo.common.FileResult;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.SysUserRole;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.ISysUserRoleService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.DateUtil;
import com.zjc.friend.demo.util.PasswordEncrypt;
import com.zjc.friend.demo.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController {

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${com.zjc.friend.web-root}")
    private String webRoot;
//
//    @Autowired
//    private SessionRegistry sessionRegistry;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/register")
    @ResponseBody
    public ResponseObject register(Userinfo user, MultipartFile icon) throws IOException, NoSuchAlgorithmException {
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("username", user.getUsername()).or().eq("phone", user.getPhone()));
        List<Userinfo> list = userService.list(queryWrapper);//查找user表里的数据 where username=? or
        if (list.size() > 0) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "账号/电话号码已经有人注册!");
        }
        FileResult fileResult=null;
        if(StringUtils.isNotBlank(icon.getOriginalFilename())){
            fileResult = fileOperator.uploadFile(icon.getInputStream(), icon.getOriginalFilename());
            user.setUserPic(fileResult.getRelativePath());
        }
        user.setCreateTime(new Date());
        user.setStatus(true);
        user.setPassword(PasswordEncrypt.encodeByMd5(user.getPassword()));
        SysUserRole sysUserRole=new SysUserRole();
        userService.save(user);//保存用户信息
        sysUserRole.setRoleId(2l);
        sysUserRole.setUserId(user.getId());
        sysUserRoleService.save(sysUserRole);
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "添加成功");
    }

    /**
     * 验证电话号码
     *
     * @param phone
     * @return
     */
    @RequestMapping("/verificationPhone")
    public ResponseObject verificationPhone(@RequestParam String phone) {
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone); //相当于phone=#{phone}
        int count = userService.count(wrapper); //查找phone的数量
        if (count > 0) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "电话号码已经有人注册!");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "");
    }

    /**
     * 验证电话号码
     *
     * @param username
     * @return
     */
    @RequestMapping("/erificationUsername")
    public ResponseObject verificationUsername(@RequestParam String username) {
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username); //username=#{username}
        int count = userService.count(wrapper); //查找username的数量
        if (count > 0) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "该账号已经有人注册!");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "");
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
        if(!userinfo.getStatus()){
            return new ResponseObject(StatusCode.FAILED.getCode(),"你已经被禁用!");
        }
        QueryWrapper<SysUserRole> wrapper1=new QueryWrapper<>();
        wrapper1.eq("user_id",userinfo.getId());
        SysUserRole one = sysUserRoleService.getOne(wrapper1);
        System.out.println(one);
        if(one.getRoleId().equals(1l)){
            return new ResponseObject(StatusCode.FAILED.getCode(), "登陆失败", userinfo);
        }
        HttpSession session = httpServletRequest.getSession();
            session.setAttribute("username",username);
            session.setAttribute("role",one.getRoleId());
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "登陆成功", userinfo);
    }

    /**
     * 查看个人信息
     *
     * @return
     */
    @RequestMapping("/personInfo")
    public ModelAndView userInfo(Model model) {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        if(StringUtils.isNotBlank(userInfo.getUserPic())){
            userInfo.setUserPic(webRoot + userInfo.getUserPic());
        }
        model.addAttribute("info", userInfo);
        return new ModelAndView("/friend/html/personal", "user", model);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping("/updateUserInfo")
    @ResponseBody
    @Transactional
    public ResponseObject updateUserInfo(Userinfo user, @RequestParam(required = false) MultipartFile icon) throws Exception {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        System.out.println("生日:" + icon.getOriginalFilename());
        UpdateWrapper<Userinfo> wrapper = new UpdateWrapper<>();
        if (!icon.getOriginalFilename().isEmpty()) {
            FileResult fileResult = fileOperator.uploadFile(icon.getInputStream(), icon.getOriginalFilename());
            if (null != fileResult) {
                wrapper.set("user_pic", fileResult.getRelativePath());
            }
        }

        if (StringUtils.isNotBlank(user.getPetName())) {
            if (!user.getPetName().equals(userInfo.getPetName())) {
                ResponseObject responseObject = verificationUsername(user.getUsername());
                if (responseObject.getStatus() != 200) {
                    return responseObject;
                }
                wrapper.set("pet_name", user.getPetName());
            }
        }
        if (StringUtils.isNotBlank(user.getPhone())) {
            if (!user.getPhone().equals(userInfo.getPhone())) {
                ResponseObject responseObject = verificationPhone(user.getPhone());
                if (responseObject.getStatus() != 200) {
                    return responseObject;
                }
                wrapper.set("phone", user.getPhone());
            }
        }
        if (StringUtils.isNotBlank(user.getUserPic())) {
            wrapper.set("user_pic", user.getUserPic());
        }
        if (StringUtils.isNotBlank(user.getAddress())) {
            wrapper.set("address", user.getAddress());
        }
        if (StringUtils.isNotBlank(user.getBirthdayTime())) {
            Long aLong = DateUtil.dateToStamp(user.getBirthdayTime());
            Date date = DateUtil.strToDate(user.getBirthdayTime());
            wrapper.set("birthday", date);
        }
        if (StringUtils.isNotBlank(user.getSex())) {
            wrapper.set("sex", user.getSex());
        }
        wrapper.eq("id", userInfo.getId());
        userService.update(wrapper);
        user.setUserPic(webRoot + user.getUserPic());
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "更新成功", user);
    }

    /**
     * 更新密码
     *
     * @param password
     * @param passwordAgain
     * @return
     */
    @RequestMapping("/updatePassword")
    @ResponseBody
    public ResponseObject updatePassword(@RequestParam String password, @RequestParam String passwordAgain) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (!password.equals(passwordAgain)) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "两个密码不一致!");
        }
        Userinfo userInfo = currentUser.getUserInfo(userService);
        String encode = PasswordEncrypt.encodeByMd5(password);
        UpdateWrapper<Userinfo> wrapper = new UpdateWrapper<>();
        wrapper.set("password", encode);
        wrapper.eq("id",userInfo.getId());
        boolean update = userService.update(wrapper);
        if (!update) {
            return new ResponseObject(StatusCode.FAILED.getCode(), "更新失败!");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "更新失败!");
    }

    /**
     * 查找全部登陆的信息
     *
     * @return
     */
    @RequestMapping("/selectAllUser")
    @ResponseBody
    public ResponseObject selectAllUser() {
//    List<String> list = sessionRegistry.getAllPrincipals().stream()
//            .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
//            .map(Object::toString)
//            .collect(Collectors.toList());
//        for (int i = 0; i < sessionRegistry.getAllPrincipals().size(); i++) {
////            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserDetails principal = (UserDetails) sessionRegistry.getAllPrincipals().get(i);
//        }
//        for (int i = 0; i < list.size(); i++) {
//        String s = list.get(i);
//        System.out.println("s:" + s);
//        }
//        return new ResponseObject(StatusCode.SUCCESS.getCode(), "", list);
        return null;
        }

    /**
     *
      * @param session
     * @param sessionStatus
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus) {
        session.removeAttribute("username");
        session.removeAttribute("role");
        sessionStatus.setComplete();
        return "redirect:/login";
    }

    @RequestMapping("/show")
    public String show() {
        System.out.println("111");
        return "html/dynamic";
    }

    /**
     * 查找用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/queryUserById")
    @ResponseBody
    public ResponseObject queryById(@RequestParam Long id) {
        Userinfo user = userService.getById(id);
        user.setUserPic(webRoot + user.getUserPic());
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功", user);
    }


}
