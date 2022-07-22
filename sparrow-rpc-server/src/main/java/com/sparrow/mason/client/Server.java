package com.sparrow.mason.client;

import com.sparrow.mason.api.RpcAccessor;
import com.sparrow.mason.api.UserService;
import com.sparrow.mason.api.spi.SpiSupport;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;

/**
 * @author chengwei_shen
 * @date 2022/7/16 21:57
 **/
@Slf4j
public class Server {
    public static void main(String[] args) throws Exception {
        try(RpcAccessor rpcAccessor = SpiSupport.load(RpcAccessor.class);
            Closeable closeable = rpcAccessor.start()){
            UserServiceImpl userService = new UserServiceImpl();
            rpcAccessor.addRpcService(userService.getServiceSign(), userService, UserService.class);
            System.in.read();
            log.info("Service stop");
        }
    }
}
