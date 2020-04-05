package com.zjc.friend.demo.constant;

/**
 * 操作状态码。
 */
public enum StatusCode {
    SUCCESS(200, "成功"),
    FAILED(400, "错误");

    private int code;
    private String msg;

    private StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
