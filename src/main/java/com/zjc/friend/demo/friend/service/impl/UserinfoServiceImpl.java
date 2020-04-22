package com.zjc.friend.demo.friend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.mapper.UserinfoMapper;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.friend.demo.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo> implements IUserinfoService {

    @Autowired
    private UserinfoMapper userinfoMapper;

    @Override
    public Userinfo findByUserName(String userName) {
        return userinfoMapper.findByUserName(userName);
    }

    @Override
    public IPage<Userinfo> getAllUsers(PageInfo pageInfo) {
        Page page=new Page();
        page.setCurrent(pageInfo.getPageIndex());
        page.setSize(pageInfo.getPageSize());
        return userinfoMapper.getAllUsers(page);
    }
}
