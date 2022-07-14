package com.sparrow.mason.core;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * @author chengwei_shen
 * @date 2022/7/13 16:31
 **/
public interface TransportClient extends Closeable {
    /**
     * 创建Transport通道对外使用
     *
     * @param address
     * @param connectionTimeout
     * @return
     */
    RpcTransport createTransport(InetSocketAddress address, long connectionTimeout);
}
