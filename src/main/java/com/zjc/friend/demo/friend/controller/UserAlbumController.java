package com.zjc.friend.demo.friend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjc.friend.demo.common.FileOperator;
import com.zjc.friend.demo.common.FileResult;
import com.zjc.friend.demo.common.PermissionConstants;
import com.zjc.friend.demo.constant.StatusCode;
import com.zjc.friend.demo.entity.UserAlbum;
import com.zjc.friend.demo.entity.Userinfo;
import com.zjc.friend.demo.friend.service.IUserAlbumService;
import com.zjc.friend.demo.friend.service.IUserinfoService;
import com.zjc.friend.demo.common.CurrentUser;
import com.zjc.friend.demo.security.RequiredPermission;
import com.zjc.friend.demo.util.PageInfo;
import com.zjc.friend.demo.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 用户相册 前端控制器
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Controller
@RequestMapping("/user-album")
public class UserAlbumController {

    @Autowired
    private IUserinfoService userService;

    @Autowired
    private IUserAlbumService userAlbumService;

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private CurrentUser currentUser;

    @Value("${com.zjc.friend.image-path}")
    private String imagePath;

    @Value("${com.zjc.friend.web-root}")
    private String webRoot;


    /**
     * 查找相册信息
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryAlbumInfo")
    public ModelAndView queryAlbuInfo(@RequestParam Long pageIndex, @RequestParam Long pageSize, Model model) {
        Userinfo userInfo = currentUser.getUserInfo(userService); //通过登录session查找用户信息
        QueryWrapper<UserAlbum> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userInfo.getId());
        PageInfo info = new PageInfo();
        info.setPageIndex(pageIndex);
        info.setPageSize(pageSize);
        Page page = new Page();
        page.setCurrent(pageIndex);
        page.setSize(pageSize);
        IPage<UserAlbum> page1 = userAlbumService.page(page, wrapper);
        List<UserAlbum> albums = new ArrayList<>();
        for (int i = 0; i < page1.getRecords().size(); i++) {
            UserAlbum album = page1.getRecords().get(i);
            album.setAlbumPic(webRoot + album.getAlbumPic());
            albums.add(album);
        }
        model.addAttribute("album", albums);
        info.setTotalCount(page1.getTotal());
        model.addAttribute("page", page);
        return new ModelAndView("/friend/html/album", "albumInfo", model);
    }

    /**
     * 增加相册信息
     *
     * @param files
     * @param title
     * @return
     */
    @RequestMapping("/addAlbumInfo")
    @ResponseBody
    @Transactional
    public ResponseObject addAlbumInfo(@RequestParam("file") MultipartFile files, @RequestParam String title) throws IOException {
        Userinfo userInfo = currentUser.getUserInfo(userService);
        FileResult fileResult = fileOperator.uploadFile(files.getInputStream(), files.getOriginalFilename());
        UserAlbum album = new UserAlbum();
        if (null != fileResult) {
            album.setUserId(userInfo.getId());
            album.setAlbumPic(fileResult.getRelativePath());
            album.setCreateTime(new Date());
            album.setTitle(title);
            album.setPicName(files.getOriginalFilename());
            boolean b = userAlbumService.save(album);
            if (!b) {
                throw new RuntimeException("添加错误!");
            }
        }
        Date d = new Date();
        album.setAlbumPic(webRoot + album.getAlbumPic());
        album.setTime(new DateFormatter("yyyy/MM/dd hh:mm:ss").print(album.getCreateTime(), Locale.CHINA));
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "添加成功", album);
    }

    /**
     * 删除相册图片
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteAlbumPic")
    @Transactional
    @ResponseBody
    public ResponseObject deleteAlbumPic(@RequestParam Long id) {
        boolean b = userAlbumService.removeById(id);
        if (!b) {
            throw new RuntimeException("删除失败");
        }
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "删除成功");
    }

}
