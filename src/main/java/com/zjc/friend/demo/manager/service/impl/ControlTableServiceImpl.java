package com.zjc.friend.demo.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.friend.demo.constant.JsonData;
import com.zjc.friend.demo.entity.ControlTable;
import com.zjc.friend.demo.manager.service.IControlTableService;
import com.zjc.friend.demo.mapper.ControlTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjc
 * @since 2020-03-11
 */
@Service
public class ControlTableServiceImpl extends ServiceImpl<ControlTableMapper, ControlTable> implements IControlTableService {

}
