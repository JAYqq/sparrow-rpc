package com.sparrow.mason.core.serializer;

import com.sparrow.mason.namesrv.impl.FileNameService;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author chengwei_shen
 * @date 2022/7/20 15:40
 **/
public class NameSrvTest {
    @Test
    public void testFileNameService() throws URISyntaxException {
        FileNameService nameService = new FileNameService();
        nameService.registerServer("aaa", new URI("localhost"));
        URI uri = nameService.seekService("aaa");
    }
}
