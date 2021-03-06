package com.zjc.friend.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.UserDynamic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.friend.demo.util.PageInfo;

import java.util.List;

/**
 * <p>
 * 用户动态 服务类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface IUserDynamicService extends IService<UserDynamic> {

    List<UserDynamicDto> queryDynamicInfo(Long id);
}
