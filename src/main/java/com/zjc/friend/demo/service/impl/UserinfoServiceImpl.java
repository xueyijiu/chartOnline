package com.zjc.friend.demo.service.impl;

import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.mapper.UserDynamicMapper;
import com.zjc.friend.demo.mapper.UserinfoMapper;
import com.zjc.friend.demo.service.IUserinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.friend.demo.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
