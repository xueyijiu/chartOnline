package com.zjc.friend.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.friend.demo.entity.Msg;
import com.zjc.friend.demo.entity.SysRole;
import com.zjc.friend.demo.entity.SysUserRole;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.service.ISysRoleService;
import com.zjc.friend.demo.service.ISysUserRoleService;
import com.zjc.friend.demo.service.IUserinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @RequestMapping("/")
    public String showHome() {
        System.out.println("1111");
        return "html/top";
    }

    @GetMapping("/login")
    public ModelAndView showLogin(String error) {
        ModelAndView modelAndView = new ModelAndView("html/login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    @RequestMapping("/login1")
    public String showLogin1() {
        System.out.println("登录");
        return "login1";
    }

    @RequestMapping("/login/error")
    @ResponseBody
    public Msg<?> loginError(HttpServletRequest request) {
        AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        return new Msg<>(false, exception.getMessage(), exception.toString());
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
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

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @ResponseBody
    public String printTeacher() {
        return "如果你看见这句话，说明你有ROLE_TEACHER角色";
    }

    @GetMapping("/adminOrTeacher")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TEACHER')")
    @ResponseBody
    public String printAdminAndTeacher() {
        return "如果你看见这句话，说明你有ROLE_ADMIN或ROLE_TEACHER和角色";
    }
}