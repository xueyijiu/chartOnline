package com.zjc.friend.demo.manager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.ControlTable;
import com.zjc.friend.demo.manager.service.IControlTableService;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/manage/control-table")
@RequiredPermission(PermissionConstants.ADMIN)
public class ManagerControlTableController {

    @Autowired
    private IControlTableService controlTableService;

    /**
     * 查找违规字段
     * @param message
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/violationList")
    public Map<String, Object> violationList(@RequestParam(required = false) String message, @RequestParam Long page,
                                             @RequestParam Long limit, @RequestParam(required = false) Boolean status){
        QueryWrapper<ControlTable> wrapper=new QueryWrapper<>();
        Page<ControlTable> page1=new Page<>();
        page1.setCurrent(page);
        page1.setSize(limit);
        if(StringUtils.isNotBlank(message)){
            wrapper.like("content",message);
        }
        if(null!=status){
            wrapper.eq("status",status);
        }
        IPage<ControlTable> page2 = controlTableService.page(page1, wrapper);
        List<ControlTable> controlTableList=new ArrayList<>();
        for (ControlTable controlTable:page2.getRecords()) {
            System.out.println(controlTable);
            if(controlTable.getStatus()){
                controlTable.setStatusString("可用");
            }
            else{
                controlTable.setStatusString("不可用");
            }
            controlTableList.add(controlTable);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        data.put("count", page2.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        data.put("data", controlTableList);
        return data;
    }

    /**
     * 新增违规内容
     * @param content
     * @param status
     * @return
     */
    @RequestMapping("/insertViolation")
    public ResponseObject insertViolation(@RequestParam String content, @RequestParam Boolean status){
        ControlTable controlTable=new ControlTable();
        controlTable.setStatus(status);
        controlTable.setContent(content);
        controlTableService.save(controlTable);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"");
    }

    /**
     * 更新违规内容
     * @param content
     * @param status
     * @return
     */
    @RequestMapping("/updateViolation")
    public ResponseObject insertViolation(@RequestParam Long id, @RequestParam(required = false) Boolean status,
                                          @RequestParam(required = false) String content){
        UpdateWrapper<ControlTable> wrapper=new UpdateWrapper<>();
        if(null!=status){
            wrapper.set("status",status);
        }
        if(StringUtils.isNotBlank(content)){
            wrapper.set("content",content);
        }
        wrapper.eq("id",id);
       controlTableService.update(wrapper);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"");
    }

    /**
     * 删除违规内容
     * @param id
     * @return
     */
    @RequestMapping("/deleteViolation")
    public ResponseObject deleteViolation(@RequestParam Long id){
        controlTableService.removeById(id);
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"");
    }

    /**
     * 修改多个违规内容的状态
     * @param data
     * @return
     */
    @RequestMapping("/updateBachViolation")
    @Transactional
    public ResponseObject updateBachViolation(String data ,Boolean status){
        Gson gson=new Gson();
       List<ControlTable> controlTables=gson.fromJson(data, new TypeToken<List<ControlTable>>() {}.getType());
        if(controlTables.size()<=0){
            return new ResponseObject(StatusCode.FAILED.getCode(),"系统错误");
        }
        for (int i = 0; i <controlTables.size() ; i++) {
            ControlTable controlTable = controlTables.get(i);
            UpdateWrapper<ControlTable> wrapper=new UpdateWrapper<>();
            wrapper.set("status",status);
            wrapper.eq("id",controlTable.getId());
            if(!controlTableService.update(wrapper)){
                throw new RuntimeException("更新错误");
            }
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"");
    }

    /**
     * 修改多个违规内容的状态
     * @param data
     * @return
     */
    @RequestMapping("/deleteBachViolation")
    @Transactional
    public ResponseObject deleteBachViolation(@RequestParam String data){
        Gson gson=new Gson();
        List<ControlTable> controlTables=gson.fromJson(data, new TypeToken<List<ControlTable>>() {}.getType());
        if(controlTables.size()<=0){
            return new ResponseObject(StatusCode.FAILED.getCode(),"系统错误");
        }
        for (ControlTable controlTable:controlTables) {
            if(!controlTableService.removeById(controlTable.getId())){
                throw  new RuntimeException("删除失败");
            }
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(),"");
    }

}
