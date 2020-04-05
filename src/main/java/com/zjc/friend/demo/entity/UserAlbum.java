package com.zjc.friend.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户相册
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserAlbum implements Serializable {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 相册图片地址
     */
    private String albumPic;

    /**
     * 创建时间
     */
    private Date createTime;

//    /**
//     * 相册id
//     */
//    private String albumId;

    /**
     * 相片标题
     */
    private String title;

    /**
     * 相片名称
     */
    private String picName;

    @TableField(exist = false)
    private String time;


}
