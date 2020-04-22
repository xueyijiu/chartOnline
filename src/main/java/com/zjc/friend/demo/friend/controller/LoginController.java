package com.zjc.friend.demo.friend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.entity.Msg;
import com.zjc.friend.demo.entity.SysRole;
import com.zjc.friend.demo.entity.SysUserRole;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.ISysRoleService;
import com.zjc.friend.demo.friend.service.ISysUserRoleService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.security.RequiredPermission;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jitwxs
 * @date 2018/3/30 1:30
 */
@Controller
public class LoginController {
    @Autowired
    private IUserinfoService userService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISysRoleService roleService;

//    @RequestMapping("/")
//    public String showHome() {
//        System.out.println("1111");
//        return "/friend/html/top";
//    }

    @GetMapping("/login")
    public ModelAndView showLogin(String error) {
        ModelAndView modelAndView = new ModelAndView("/friend/html/login");
//        modelAndView.addObject("error", error);
        return modelAndView;
    }

    @RequestMapping("/login1")
    public String showLogin1() {
        System.out.println("登录");
        return "/friend/login1";
    }

    @RequestMapping("/login/error")
    @ResponseBody
    public Msg<?> loginError(HttpServletRequest request) {
//        AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
//
//        return new Msg<>(false, exception.getMessage(), exception.toString());
        return null;
    }

//    @RequestMapping("/register")
//    public String showRegister() {
//        return "html/register";
//    }

//    @PostMapping("/register")
//    public Object register(Userinfo user, Integer[] roles) throws Exception{
//        String name = user.getUsername();
//
//        if(StringUtils.isBlank(name) || StringUtils.isBlank(user.getPassword())) {
//            throw new Exception("输入数据错误");
//        }
//        QueryWrapper<Userinfo> wrapper=new QueryWrapper<>();
//        wrapper.eq("username",name);
//        if(userService.getOne(wrapper) != null) {
//            throw new Exception("用户名已被注册");
//        }
//
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        userService.save(user);
//        Long id = userService.getOne(wrapper).getId();
//        for(long roleId : roles) {
//            SysUserRole userRole=new SysUserRole();
//            userRole.setUserId(id);
//            userRole.setRoleId(roleId);
//            userRoleService.save(userRole);
//        }
//
//        return "redirect:/login";
//    }

    @GetMapping("/userInfo")
    @ResponseBody
    public Msg<?> getSelfUserInfo() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        String name="";
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);
        Userinfo user = userService.getOne(wrapper);
        QueryWrapper<SysUserRole> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("id", user.getId());
        List<SysUserRole> userRoles = userRoleService.list(wrapper1);
        List<SysRole> roles = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
//            QueryWrapper<SysUserRole> wrapper2=new QueryWrapper<>();
//            wrapper2.eq("id",userRole.getRoleId());
            SysRole role = roleService.getById(userRole.getRoleId());
            roles.add(role);
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("name", name);
        map.put("roles", roles);

        return new Msg<>(true, null, map);
    }

}