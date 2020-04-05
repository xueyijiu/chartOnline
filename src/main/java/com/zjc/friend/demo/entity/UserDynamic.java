package com.zjc.friend.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户动态
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Valid
public class UserDynamic implements Serializable {

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


}
