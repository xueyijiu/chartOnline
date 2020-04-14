package com.zjc.friend.demo.friend.service.impl;

import com.zjc.friend.demo.entity.Comment;
import com.zjc.friend.demo.mapper.CommentMapper;
import com.zjc.friend.demo.friend.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
