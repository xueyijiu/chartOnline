package com.zjc.friend.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.UserDynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户动态 Mapper 接口
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface UserDynamicMapper extends BaseMapper<UserDynamic> {

    List<UserDynamicDto> queryDynamicInfo(@Param("ids")Long ids);
}
