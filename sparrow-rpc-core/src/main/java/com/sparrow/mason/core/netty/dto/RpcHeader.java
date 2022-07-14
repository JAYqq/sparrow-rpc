package com.sparrow.mason.core.netty.dto;

/**
 * @author chengwei_shen
 * @date 2022/7/13 20:06
 **/
public class RpcHeader {
    /**
     * 版本号
     */
    String version;
    /**
     * traceId
     */
    String traceId;
    /**
     * 类型
     */
    String type;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
