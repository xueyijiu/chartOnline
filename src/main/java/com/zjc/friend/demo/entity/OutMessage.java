package com.zjc.friend.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 12:36 2020/3/6
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
@Data
public class OutMessage {

    private Long id;

    private String from;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time = new Date();

    public OutMessage() {
    }

    public OutMessage(String content) {
        this.content = content;

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
