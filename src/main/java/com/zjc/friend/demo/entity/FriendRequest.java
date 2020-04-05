package com.zjc.friend.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 好友申请表
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FriendRequest implements Serializable {

    private Long id;

    /**
     * 分组表id
     */
    private Long categoryId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 好友备注
     */
    private String friendComment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 申请状态，0为未处理，1为同意，2为拒绝
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer requestStatus;

    /**
     * 好友状态 0是正常1是删除
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer status;

    @TableField(exist = false)
    private Userinfo userinfo;

    private Boolean code;

    @TableField(exist = false)
    private Integer messageCount;


}
