package com.zjc.friend.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.Userinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface UserinfoMapper extends BaseMapper<Userinfo> {

    /**
     * 通过username查找user信息
     * @param userName
     * @return
     */
    Userinfo findByUserName(String userName);

    /**
     * 获取全部用户信息
     * Ipage（分页）
     * @return
     */
    IPage<Userinfo> getAllUsers(Page page);

}
