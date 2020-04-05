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

    private String from;

    private String to;

    private String content;

    private Date time;
}
