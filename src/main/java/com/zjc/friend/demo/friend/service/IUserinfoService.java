package com.zjc.friend.demo.friend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjc.friend.demo.entity.Userinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.friend.demo.util.PageInfo;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface IUserinfoService extends IService<Userinfo> {
    /**
     * 查找用户信息
     * @param userName （用户名）
     * @return
     */
    Userinfo findByUserName(String userName);

    IPage<Userinfo> getAllUsers(PageInfo pageInfo);
}
