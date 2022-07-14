package com.sparrow.mason.core.netty.dto;

/**
 * 错误枚举类
 *
 * @author chengwei_shen
 * @date 2022/7/13 20:12
 **/
public enum RspCode {
    SUCCESS(1, "SUCCESS"),
    ERROR(-1, "ERROR");

    RspCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    int code;
    String message;
}
