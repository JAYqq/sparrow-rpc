package com.sparrow.mason.core;

import com.sparrow.mason.api.NameService;
import com.sparrow.mason.api.RpcAccessor;
import com.sparrow.mason.api.spi.SpiSupport;
import com.sparrow.mason.core.client.ProxyFactory;
import com.sparrow.mason.api.ServiceMetaInfo;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * Accessor的主要功能在于对外提供统一的API去访问RPC核心相关的方法，控制权限收口，
 * 然后像SPI的配置机制也可以统一放在该模块下使用
 *
 * @author chengwei_shen
 * @date 2022/7/13 15:59
 **/
public class NettyRpcAccessor implements RpcAccessor {
    private final int port = 8888;

    @Override
    public <T> T getRemoteService(ServiceMetaInfo metaInfo, Class<T> clazz) {
        NameService nameService = SpiSupport.load(NameService.class);
        String serviceSign = metaInfo.getServiceSign();
        URI uri = nameService.seekService(serviceSign);
        TransportClient transportClient = SpiSupport.load(TransportClient.class);
        InetSocketAddress address = new InetSocketAddress(uri.getHost(), uri.getPort());
        RpcTransport transport = transportClient.createTransport(address, 3000);
        ProxyFactory proxyFactory = SpiSupport.load(ProxyFactory.class);
        proxyFactory.setRpcTransport(transport);
        return proxyFactory.createProxy(clazz, metaInfo);
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
