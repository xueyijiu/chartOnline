package com.zjc.friend.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建时间
     */
    private Date endTime;

    /**
     * 删除状态，0为未删除，1为删除
     */
    private Integer deleteStatus;

    /**
     * 动态内容
     */
    private String dynaminContent;

    private String username;

    private List<DynamicFile> dynamicFiles;

    private List<Comment> commentList;

    private Boolean isNotMyself = false;

    private Userinfo userinfo;

    private String deleteStatusString;

    @Override
    public String toString() {
        return "UserDynamicDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", dynamicTitle='" + dynamicTitle + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", deleteStatus=" + deleteStatus +
                ", dynaminContent='" + dynaminContent + '\'' +
                ", username='" + username + '\'' +
                ", dynamicFiles=" + dynamicFiles +
                ", commentList=" + commentList +
                ", isNotMyself=" + isNotMyself +
                ", userinfo=" + userinfo +
                ", deleteStatusString='" + deleteStatusString + '\'' +
                '}';
    }
}
