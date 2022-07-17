package com.sparrow.mason.core.netty.handler;

import com.sparrow.mason.core.ServiceHub;
import com.sparrow.mason.core.netty.dto.RpcCommand;
import com.sparrow.mason.core.netty.dto.RpcRequest;
import com.sparrow.mason.core.netty.dto.RpcResponse;
import com.sparrow.mason.core.netty.dto.RspCode;
import com.sparrow.mason.core.serialize.SerializeSupport;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 请求处理器，每一个从netty client channel进来的请求都要在这里处理
 *
 * @author chengwei_shen
 * @date 2022/7/14 11:44
 **/
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcCommand> {
    /**
     * 进行真正方法调用
     *
     * @param channelHandlerContext the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                              belongs to
     * @param command               the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcCommand command) throws Exception {
        RpcResponse response = invokeService(command);
        channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture->{
            if(!channelFuture.isSuccess()){
                log.warn("Write Channel Error",channelFuture.cause());
                channelHandlerContext.channel().close();
            }
        });
    }

    private RpcResponse invokeService(RpcCommand command) {
        RpcResponse response = new RpcResponse();
        response.setHeader(command.getHeader());
        RpcRequest rpcRequest = SerializeSupport.parse(command.getData());
        Object[] args = SerializeSupport.parse(rpcRequest.getParameters());
        Object service = ServiceHub.getInstance().getService(rpcRequest.buildServiceSign());
        if (Objects.isNull(service)) {
            response.setCode(RspCode.UNKNOWN_SERVICE.getCode());
            response.setErrorMsg(RspCode.UNKNOWN_SERVICE.getMessage());
            log.warn("Service named {} not found!", rpcRequest.buildServiceSign());
            return response;
        }
        try {
            Class[] argClass = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argClass[i] = args[i].getClass();
            }
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), argClass);
            Object ans = method.invoke(service, args);
            response.setData(SerializeSupport.serialize(ans));
        } catch (Exception e) {
            log.warn("invoke error", e);
            response.setCode(RspCode.UNKNOWN_SERVICE.getCode());
            response.setErrorMsg(RspCode.UNKNOWN_SERVICE.getMessage());
        }
        return response;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
