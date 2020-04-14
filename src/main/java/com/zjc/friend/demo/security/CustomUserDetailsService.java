//package com.zjc.friend.demo.security;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.zjc.friend.demo.entity.SysUserRole;
//import com.zjc.friend.demo.entity.Userinfo;
//import com.zjc.friend.demo.friend.service.ISysRoleService;
//import com.zjc.friend.demo.friend.service.ISysUserRoleService;
//import com.zjc.friend.demo.friend.service.IUserinfoService;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.userdetails.User;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///**
// * @author jitwxs
// * @date 2018/3/30 9:17
// */
//@Service("userDetailsService")
//public class CustomUserDetailsService implements UserDetailsService {
//    @Autowired
//    private IUserinfoService userService;
//
//    @Autowired
//    private ISysRoleService roleService;
//
//    @Autowired
//    private ISysUserRoleService userRoleService;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        QueryWrapper<Userinfo> wrapper = new QueryWrapper<>();
//        wrapper.eq("username", s);
//        Userinfo user = userService.getOne(wrapper);
//        // 判断用户是否存在
//        if (user == null) {
//            throw new UsernameNotFoundException("用户名不存在");
//        }
//        if(!user.getStatus()){
//            throw new UsernameNotFoundException("用户名已经被禁用!");
//        }
//        // 添加权限
//        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", user.getId());
//        List<SysUserRole> userRoles = userRoleService.list(queryWrapper);
////        for (SysUserRole userRole : userRoles) {
////            SysRole role = roleService.getById(userRole.getRoleId());
////            authorities.add(new SimpleGrantedAuthority(role.getName()));
////        }
//        // 返回UserDetails实现类
//        return new User(user.getUsername(), user.getPassword(), authorities);
//    }
//}
