package com.sparrow.mason.core.client.proxy;

import com.sparrow.mason.core.RpcTransport;
import com.sparrow.mason.core.client.ProxyFactory;
import com.sparrow.mason.api.ServiceMetaInfo;

public class CglibProxyFactory implements ProxyFactory {
    RpcTransport rpcTransport;

    @Override
    public <T> T createProxy(Class<T> clazz, ServiceMetaInfo metaInfo) {
        CglibRpcProxy proxy = new CglibRpcProxy(clazz, metaInfo, rpcTransport);
        return (T) proxy.getProxy();
    }

    public RpcTransport getRpcTransport() {
        return rpcTransport;
    }

    public void setRpcTransport(RpcTransport rpcTransport) {
        this.rpcTransport = rpcTransport;
    }
}
