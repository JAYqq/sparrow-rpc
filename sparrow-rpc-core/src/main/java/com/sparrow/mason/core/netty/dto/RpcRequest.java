package com.sparrow.mason.core.netty.dto;

/**
 * @author chengwei_shen
 * @date 2022/7/13 20:06
 **/
public class RpcRequest {
    RpcHeader header;
    String nameSpace;
    String serviceName;
    byte[] data;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * 构造服务签名
     *
     * @param request
     * @return
     */
    public static String buildServiceSign(RpcRequest request) {
        String fmt = "%s:%s";
        return String.format(request.getNameSpace(), request.getServiceName());
    }
}
