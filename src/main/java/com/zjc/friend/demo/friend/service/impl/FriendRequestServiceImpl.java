package com.zjc.friend.demo.friend.service.impl;

import com.zjc.friend.demo.entity.FriendRequest;
import com.zjc.friend.demo.mapper.FriendRequestMapper;
import com.zjc.friend.demo.friend.service.IFriendRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 好友申请表 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements IFriendRequestService {

}
