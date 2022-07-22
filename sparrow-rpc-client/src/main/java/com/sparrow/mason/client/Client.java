package com.sparrow.mason.client;

import com.sparrow.mason.api.RpcAccessor;
import com.sparrow.mason.api.ServiceMetaInfo;
import com.sparrow.mason.api.UserService;
import com.sparrow.mason.api.dto.User;
import com.sparrow.mason.api.spi.SpiSupport;

import java.io.IOException;

/**
 * @author chengwei_shen
 * @date 2022/7/20 15:58
 **/
public class Client {
    public static void main(String[] args) throws Exception {
        try (RpcAccessor rpcAccessor = SpiSupport.load(RpcAccessor.class)) {
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo("Sparrow", "UserService");
            UserService userService = rpcAccessor.getRemoteService(serviceMetaInfo, UserService.class);
            User mason = userService.getUserByName("Mason");
        }
    }
}
