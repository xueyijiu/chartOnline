package com.zjc.friend.demo.security;

import com.alibaba.fastjson.JSON;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.service.ISysUserRoleService;
import com.zjc.friend.demo.service.IUserinfoService;
import com.zjc.friend.demo.service.impl.UserinfoServiceImpl;
import com.zjc.friend.demo.util.CurrentUser;
import com.zjc.friend.demo.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private IUserinfoService userRepository;

    @Autowired
    private ISysUserRoleService userAuxService;

    @Autowired
    private SaturnUserDetailsService userDetailsService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Userinfo user = CurrentUser.getUserInfo(userRepository);
        ResponseObject res = ResponseObject.getSucceed();
        System.out.println("username:" + ((UserDetails) principal).getUsername());
        res.setMessage("登录成功");
        res.setData(user);
        response.setHeader("sessionid", request.getSession().getId());
//        UserAux userAux =new UserAux();
//        userAux.setUserId(user.getId());
//        userAux.setLastLogon(new Date());
//        userAuxService.save(userAux);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(res));
        System.out.println("登录信息：" + JSON.toJSONString(res));
//        RequestCache cache = new HttpSessionRequestCache();
//        SavedRequest savedRequest = cache.getRequest(request, response);
        ModelAndView model = new ModelAndView("/main/index");
        model.addObject("info", JSON.toJSONString(res));
        response.sendRedirect("/index");
        out.flush();
        out.close();
    }
}