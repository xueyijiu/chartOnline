package com.zjc.friend.demo.friend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.dto.UserDynamicDto;
import com.zjc.friend.demo.entity.Comment;
import com.zjc.friend.demo.entity.UserDynamic;
import com.zjc.friend.demo.mapper.UserDynamicMapper;
import com.zjc.friend.demo.friend.service.IUserDynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.friend.demo.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired CommentServiceImpl commentService;


    @Override
    public IPage<UserDynamicDto> queryDynamicInfo(Page page,Long id) {
        System.out.println(page.getCurrent()+"=="+page.getSize());
        IPage<UserDynamicDto> userDynamicDtoIPage = userDynamicMapper.queryDynamicInfo(page, id);
        List<Comment> commentList=null;
        if(null==id){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.orderByAsc("create_time");
            commentList = commentService.list(wrapper);
        }
        else{
            List<Long> collect = userDynamicDtoIPage.getRecords().stream().map(UserDynamicDto::getId).collect(Collectors.toList());
            QueryWrapper<Comment> wrapper=new QueryWrapper<>();
            wrapper.in("dynamic_id",collect);
            wrapper.orderByAsc("create_time");
            commentList = commentService.list(wrapper);
        }
        List<Comment> commentList1=new ArrayList<>();
        for (UserDynamicDto userDynamicDto:userDynamicDtoIPage.getRecords()) {
            for (Comment comment:commentList) {
                if(userDynamicDto.getId().equals(comment.getDynamicId())){
                    commentList1.add(comment);
                }
            }
            userDynamicDto.setCommentList(commentList1);
        }
        return userDynamicDtoIPage;
    }

    @Override
    public IPage<UserDynamicDto> queryDynamic(PageInfo page, UserDynamic dynamic) {
        Page page1=new Page();
        page1.setCurrent(page.getPageIndex());
        page1.setSize(page.getPageSize());
        return userDynamicMapper.queryDynamic(page1,dynamic);
    }
}
