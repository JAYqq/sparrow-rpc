package com.sparrow.mason.core.netty.handler;

import com.sparrow.mason.core.netty.dto.RpcResponse;
import com.sparrow.mason.core.transport.netty.PendingRequests;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author chengwei_shen
 * @date 2022/7/18 19:12
 **/
@ChannelHandler.Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        CompletableFuture<RpcResponse> rspFuture = PendingRequests.getInstance().remove(rpcResponse.getHeader().getTraceId());
        //通过complete将reponse返回给对应future的get阻塞线程
        rspFuture.complete(rpcResponse);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            System.out.println("aaaaaa");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
