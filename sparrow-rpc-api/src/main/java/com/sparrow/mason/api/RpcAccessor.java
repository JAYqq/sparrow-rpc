package com.sparrow.mason.api;

import com.sparrow.mason.api.spi.SpiSupport;

import java.io.Closeable;
import java.net.URI;
import java.util.concurrent.TimeoutException;

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
     * @param metaInfo 服务元信息
     * @param clazz    接口
     * @param <T>      服务类型
     * @return 服务实例
     */
    <T> T getRemoteService(ServiceMetaInfo metaInfo, Class<T> clazz) throws InterruptedException, TimeoutException;

    /**
     * 注册RPC服务
     *
     * @param serviceSign 服务签名
     * @param service     服务实例
     * @param clazz       服务接口类
     * @param <T>         类型
     * @return 服务地址
     */
    <T> URI addRpcService(String serviceSign, T service, Class<T> clazz);

    /**
     * 启动RPC服务
     */
    Closeable start() throws Exception;

}
