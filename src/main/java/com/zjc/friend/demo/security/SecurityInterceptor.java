package com.zjc.friend.demo.security;

import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 10:02 2020/4/10
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserinfoService adminUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String servletPath = request.getRequestURI();
        String userName=(String)session.getAttribute("username");
        if(null==userName){
            Object userName1 = session.getAttribute("username");
            if(servletPath.contains("manage")){
                response.sendRedirect("/manage/loginHtml");
                return false;
            }
            else{
                response.sendRedirect("/loginInfo");
                return false;
            }
        }
        else{
            Long role = (Long) session.getAttribute("role");
            if(servletPath.contains("manage")){
                    if(!role.equals(1l)){
                        response.sendRedirect("/manage/loginHtml");
                        return false;
                    }
                }else{
                    if(!role.equals(2l)){
                        response.sendRedirect("/loginInfo");
                        return false;
                    }
                }
            if (hasPermission(handler,userName)) {
                return true;
            }
            //  null == request.getHeader("x-requested-with") TODO 暂时用这个来判断是否为ajax请求
            // 如果没有权限 则抛403异常 springboot会处理，跳转到 /error/403 页面
            response.sendError(HttpStatus.FORBIDDEN.value(), "无权限");
            return false;
        }
        // 验证权限

    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(Object handler,String username) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取类上的注解
            RequiredPermission annotation = handlerMethod.getBeanType().getAnnotation(RequiredPermission.class);
                if (annotation != null) {
                    // redis或数据库 中获取该用户的权限信息 并判断是否有权限
                    Userinfo byUserName = adminUserService.findByUserName(username);
                    if (!byUserName.getRoles().get(0).getId().equals(annotation.value())){
                        return false;
                    }
            }
            // 获取方法上的注解
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                return true;
            }
            System.out.println("requiredPermission:"+requiredPermission.value());
            // 如果标记了注解，则判断权限
            if (requiredPermission != null) {
                // redis或数据库 中获取该用户的权限信息 并判断是否有权限
                Userinfo byUserName = adminUserService.findByUserName(username);
                if (!byUserName.getRoles().get(0).getId().equals(requiredPermission.value())){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO
    }

}
