package com.sparrow.mason.core;

import com.sparrow.mason.api.RpcAccessor;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * @author chengwei_shen
 * @date 2022/7/13 15:59
 **/
public class NettyRpcAccessor implements RpcAccessor {
    private final int port = 8888;

    @Override
    public <T> T getRemoteService(String serviceName, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> URI addRpcService(T service, Class<T> clazz) {
        return null;
    }

    @Override
    public Closeable start() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    /**
     * 获取本地uri
     *
     * @return 本地uri
     * @throws UnknownHostException
     */
    private URI getLocalUri() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        return URI.create("rpc://" + hostAddress + ":" + port);
    }
}
