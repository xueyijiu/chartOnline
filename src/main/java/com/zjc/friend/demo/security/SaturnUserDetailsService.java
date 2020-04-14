//package com.zjc.friend.demo.security;
//
//import com.zjc.friend.demo.entity.SysRole;
//import com.zjc.friend.demo.entity.Userinfo;
//import com.zjc.friend.demo.mapper.UserinfoMapper;
//import com.zjc.friend.demo.friend.service.IUserinfoService;
//import com.zjc.friend.demo.common.CurrentUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Component
//public class SaturnUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserinfoMapper userRepository;
//
//    @Autowired
//    private IUserinfoService userinfoService;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Userinfo user = CurrentUser.getUserInfo(userinfoService);
//        if (null == user) {
//            throw new UsernameNotFoundException("user[" + username + "] not found");
//        }
//        if(!user.getStatus()){
//            throw new DisabledException("user["+username+"]被禁用");
//        }
//        List<SysRole> roles = user.getRoles();
//        Set<GrantedAuthority> authoritySet = new HashSet<>();
//        org.springframework.security.core.userdetails.User userDetails =
//                new org.springframework.security.core.userdetails.User(username, user.getPassword(), authoritySet);
//        return userDetails;
//    }
//
//}
