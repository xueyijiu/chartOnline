//package com.zjc.friend.demo.security;
//
//import com.alibaba.fastjson.JSON;
//import com.zjc.friend.demo.util.ResponseObject;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * @ Author     ：zjc
// * @ Date       ：Created in 13:33 2020/2/28
// * @ Description：
// * @ Modified By：
// * @Version: $
// */
//@Component
//public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        ResponseObject res = ResponseObject.getFailed();
//        res.setMessage(exception.getMessage());
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        System.out.println(JSON.toJSONString(res));
//        out.write(JSON.toJSONString(res));
//        out.flush();
//        out.close();
//    }
//}
