package com.zjc.friend.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zjc.friend.demo.constant.StatusCode;
import lombok.Data;

@Data
public class ResponseObject<T> {
    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageInfo pageInfo;

    public ResponseObject() {
    }

    public ResponseObject(int status, String message, T data, PageInfo pageInfo) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.pageInfo = pageInfo;
    }

    public ResponseObject(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseObject(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseObject getSucceed() {
        return new ResponseObject(StatusCode.SUCCESS.getCode(), "成功", null, null);
    }

    public static ResponseObject getFailed() {
        return new ResponseObject(StatusCode.FAILED.getCode(), "失败", null, null);
    }

    public static ResponseObject getFailed(int code, String message) {
        return new ResponseObject(code, message, null, null);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}

