package com.zjc.friend.demo.dto;

import com.zjc.friend.demo.entity.Comment;
import com.zjc.friend.demo.entity.DynamicFile;
import com.zjc.friend.demo.entity.Userinfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 14:11 2020/2/26
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
@Data
public class UserDynamicDto {
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

//    /**
//     * 动态id
//     */
//    private String dynamicId;

    /**
     * 动态标题
     */
    private String dynamicTitle;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除状态，0为未删除，1为删除
     */
    private Integer deleteStatus;

    /**
     * 动态内容
     */
    private String dynaminContent;

    private List<DynamicFile> dynamicFiles;

    private List<Comment> commentList;

    private Boolean isNotMyself = false;

    private Userinfo userinfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDynamicTitle() {
        return dynamicTitle;
    }

    public void setDynamicTitle(String dynamicTitle) {
        this.dynamicTitle = dynamicTitle;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getDynaminContent() {
        return dynaminContent;
    }

    public void setDynaminContent(String dynaminContent) {
        this.dynaminContent = dynaminContent;
    }

    public List<DynamicFile> getDynamicFiles() {
        return dynamicFiles;
    }

    public void setDynamicFiles(List<DynamicFile> dynamicFiles) {
        this.dynamicFiles = dynamicFiles;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Boolean getNotMyself() {
        return isNotMyself;
    }

    public void setNotMyself(Boolean notMyself) {
        isNotMyself = notMyself;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

}
