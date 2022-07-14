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
    public void registerServer(String serviceName, URI uri) {
        Set<URI> uris = metaDataMp.computeIfAbsent(serviceName, v -> new HashSet<>());
        uris.add(uri);
    }

    @Override
    public void unregisterServer(String serviceName, URI uri) {
        if (metaDataMp.containsKey(serviceName)) {
            metaDataMp.get(serviceName).remove(uri);
        }
    }

    /**
     * todo 负载均衡
     *
     * @param serviceName
     * @return
     */
    @Override
    public URI seekService(String serviceName) {
        if (metaDataMp.containsKey(serviceName)) {
            Set<URI> uris = metaDataMp.get(serviceName);
            return (URI) uris.toArray()[ThreadLocalRandom.current().nextInt(uris.size())];
        }
        return null;
    }
}
