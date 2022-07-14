package com.sparrow.mason.core;

import com.sparrow.mason.core.netty.dto.RpcRequest;

/**
 * @author chengwei_shen
 * @date 2022/7/13 17:18
 **/
public interface RpcTransport {
    /**
     * 发送请求
     *
     * @param request 具体请求体
     * @return response
     */
    Object send(RpcRequest request);
}
