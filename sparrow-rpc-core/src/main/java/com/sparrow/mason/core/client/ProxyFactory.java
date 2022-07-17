package com.sparrow.mason.core.client;

import com.sparrow.mason.api.ServiceMetaInfo;
import com.sparrow.mason.core.RpcTransport;

public interface ProxyFactory {
    <T> T createProxy(Class<T> clazz, ServiceMetaInfo metaInfo);

    void setRpcTransport(RpcTransport rpcTransport);
}
