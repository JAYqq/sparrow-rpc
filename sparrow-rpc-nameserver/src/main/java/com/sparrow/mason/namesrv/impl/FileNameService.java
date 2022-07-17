package com.sparrow.mason.namesrv.impl;

import com.sparrow.mason.api.NameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

/**
 * @author chengwei_shen
 * @date 2022/7/12 20:49
 **/
public class FileNameService implements NameService {
    private static final Logger logger = LoggerFactory.getLogger(FileNameService.class);
    private static File metaDataFile;

    static {
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "sparrow_rpc_service.data");
        metaDataFile = new File(file.toURI());
    }

    @Override
    public void registerServer(String serviceSign, URI uri) {
    }

    @Override
    public void unregisterServer(String serviceSign, URI uri) {

    }

    @Override
    public URI seekService(String serviceSign) {
        return null;
    }
}
