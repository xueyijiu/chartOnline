package com.zjc.friend.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private Long id;


    /**
     * 动态id
     */
    private Long dynamicId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论的用户id
     */
    private Long commentUserId;

    @TableField(exist = false)
    private String petSrc;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private boolean myselfComment = false;

    @TableField(exist = false)
    private String timeString;

    @TableField(exist = false)
    private String commentUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isMyselfComment() {
        return myselfComment;
    }

    public void setMyselfComment(boolean myselfComment) {
        this.myselfComment = myselfComment;
    }

    public String getPetSrc() {
        return petSrc;
    }

    public void setPetSrc(String petSrc) {
        this.petSrc = petSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
    }
}
