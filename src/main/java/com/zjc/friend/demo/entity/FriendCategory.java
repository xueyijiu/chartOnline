package com.zjc.friend.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 好友分组信息
 * </p>
 *
 * @author zjc
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FriendCategory implements Serializable {

    //    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 分组名称
     */
    private String categoryName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @TableField(exist = false)
    private List<FriendRequest> friendRequestList;


}
