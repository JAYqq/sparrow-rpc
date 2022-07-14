package com.sparrow.mason.api;

import com.sparrow.mason.api.spi.SpiSupport;

import java.io.Closeable;
import java.net.URI;

/**
 * @author chengwei_shen
 * @date 2022/7/13 14:59
 **/
public interface RpcAccessor extends Closeable {
    /**
     * 获取NameServer
     *
     * @return
     */
    default NameService getNameService() {
        return SpiSupport.load(NameService.class);
    }

    /**
     * 获取远程服务代理类
     *
     * @param serviceName 服务名
     * @param clazz       接口
     * @param <T>         服务类型
     * @return 服务实例
     */
    <T> T getRemoteService(String serviceName, Class<T> clazz);

    /**
     * 注册RPC服务
     *
     * @param service 服务实例
     * @param clazz   服务接口类
     * @param <T>     类型
     * @return 服务地址
     */
    <T> URI addRpcService(T service, Class<T> clazz);

    /**
     * 启动RPC服务
     */
    Closeable start();

}
