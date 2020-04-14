package com.zjc.friend.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zjc
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ControlTable implements Serializable {

//    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 控制内容
     */
    private String content;

    /**
     * 状态
     */
    private Boolean status;

    @TableField(exist = false)
    private String statusString;


}
