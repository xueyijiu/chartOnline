package com.zjc.friend.demo.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.service.IUserinfoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class CurrentUser {
    public static UserDetails getCurrentUser() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal;
    }

    /**
     * 具体规则可以自己定义 java代码
     *
     * @param newPwd 新密码
     * @param
     * @return
     */
    public static boolean checkPwd(String newPwd) {
        //判断密码是否包含数字：包含返回1，不包含返回0
        int i = newPwd.matches(".*\\d+.*") ? 1 : 0;

        //判断密码是否包含字母：包含返回1，不包含返回0
        int j = newPwd.matches(".*[a-zA-Z]+.*") ? 1 : 0;

        //判断密码是否包含特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)：包含返回1，不包含返回0
        int k = newPwd.matches(".*[_+#|<>,.?/:;'\\[\\]{}\"]+.*") ? 1 : 0;

        //判断密码长度是否在6-16位
        int l = newPwd.length();

//        //判断密码中是否包含用户名  , String loginName
//        boolean contains = newPwd.contains(loginName);

        if (i + j + k < 2 || l < 6 || l > 16) {
            return false;
        }
        return true;
    }

    public static Userinfo getUserInfo(IUserinfoService userService) {
        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("username", CurrentUser.getCurrentUser().getUsername());
        wrapper.eq("status",true);
        return userService.getOne(wrapper);
    }
}
