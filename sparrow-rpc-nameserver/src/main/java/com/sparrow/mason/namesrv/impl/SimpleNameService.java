package com.sparrow.mason.namesrv.impl;

import com.sparrow.mason.api.NameService;
import com.sparrow.mason.api.spi.Singleton;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chengwei_shen
 * @date 2022/7/12 21:02
 **/
@Singleton
public class SimpleNameService implements NameService {
    private static final Map<String, Set<URI>> metaDataMp = new ConcurrentHashMap<>();

    @Override
    public void registerServer(String serviceSign, URI uri) {
        Set<URI> uris = metaDataMp.computeIfAbsent(serviceSign, v -> new HashSet<>());
        uris.add(uri);
    }

    @Override
    public void unregisterServer(String serviceSign, URI uri) {
        if (metaDataMp.containsKey(serviceSign)) {
            metaDataMp.get(serviceSign).remove(uri);
        }
    }

    /**
     * todo 负载均衡
     *
     * @param serviceSign
     * @return
     */
    @Override
    public URI seekService(String serviceSign) {
        if (metaDataMp.containsKey(serviceSign)) {
            Set<URI> uris = metaDataMp.get(serviceSign);
            return (URI) uris.toArray()[ThreadLocalRandom.current().nextInt(uris.size())];
        }
        return null;
    }
}
