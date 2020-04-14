package com.zjc.friend.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 12:17 2020/3/6
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
@Data
public class InMessage {

    //发送人
    private String from;

    //过去信息的人
    private String to;

    //内容
    private String content;

    //时间
    private Date time;
}
