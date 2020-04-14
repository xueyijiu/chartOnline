package com.zjc.friend.demo.friend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.UserDynamic;
import com.zjc.friend.demo.mapper.UserDynamicMapper;
import com.zjc.friend.demo.friend.service.IUserDynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.friend.demo.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户动态 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Service
public class UserDynamicServiceImpl extends ServiceImpl<UserDynamicMapper, UserDynamic> implements IUserDynamicService {

    @Autowired
    private UserDynamicMapper userDynamicMapper;

    @Override
    public List<UserDynamicDto> queryDynamicInfo(Long id) {
        return userDynamicMapper.queryDynamicInfo(id);
    }

    @Override
    public IPage<UserDynamicDto> queryDynamic(PageInfo page, UserDynamic dynamic) {
        Page page1=new Page();
        page1.setCurrent(page.getPageIndex());
        page1.setSize(page.getPageSize());
        return userDynamicMapper.queryDynamic(page1,dynamic);
    }
}
