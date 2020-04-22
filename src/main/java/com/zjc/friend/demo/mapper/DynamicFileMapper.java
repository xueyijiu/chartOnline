package com.zjc.friend.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.DynamicFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
public interface DynamicFileMapper extends BaseMapper<DynamicFile> {

    List<UserDynamicDto> queryDynamicInfo(Page page, List<Long> ids);
}
