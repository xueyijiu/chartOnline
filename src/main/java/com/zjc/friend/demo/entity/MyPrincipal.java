package com.zjc.friend.demo.entity;

import java.security.Principal;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 18:49 2020/4/14
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class MyPrincipal implements Principal {
    private String loginName;

    public MyPrincipal(String loginName){
        this.loginName = loginName;
    }
    @Override
    public String getName() {
        return loginName;
    }
}
