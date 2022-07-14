package com.sparrow.mason.core.netty.dto;

import java.io.Serializable;

/**
 * @author chengwei_shen
 * @date 2022/7/13 20:06
 **/
public class RpcResponse implements Serializable {
    RpcHeader header;
    int code;
    String errorMsg;
    byte[] data;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
