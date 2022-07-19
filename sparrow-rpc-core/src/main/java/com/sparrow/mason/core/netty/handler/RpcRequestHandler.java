package com.sparrow.mason.core.netty.handler;

import com.sparrow.mason.core.ServiceHub;
import com.sparrow.mason.core.netty.dto.*;
import com.sparrow.mason.core.serialize.SerializeSupport;
import com.sparrow.mason.core.serialize.SerializerType;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
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
//这个表示这个handler在每个channel中进行共享，可参考https://learn.lianglianglee.com/%E4%B8%93%E6%A0%8F/Netty%20%E6%A0%B8%E5%BF%83%E5%8E%9F%E7%90%86%E5%89%96%E6%9E%90%E4%B8%8E%20RPC%20%E5%AE%9E%E8%B7%B5-%E5%AE%8C/30%20%20%E5%AE%9E%E8%B7%B5%E6%80%BB%E7%BB%93%EF%BC%9ANetty%20%E5%9C%A8%E9%A1%B9%E7%9B%AE%E5%BC%80%E5%8F%91%E4%B8%AD%E7%9A%84%E4%B8%80%E4%BA%9B%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5.md
@ChannelHandler.Sharable
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
        channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                log.warn("Write Channel Error", channelFuture.cause());
                channelHandlerContext.channel().close();
            }
        });
    }

    private RpcResponse invokeService(RpcCommand command) {
        RpcResponse response = new RpcResponse();
        response.setHeader(command.getHeader());
        response.getHeader().setType(CommandTypes.RPC_RESPONSE.getType());
        //指定用jdk序列化方式
        RpcRequest rpcRequest = SerializeSupport.parse(command.getData(), SerializerType.TYPE_OBJECT_ARRAY.getType());
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
            response.setData(ans);
            response.setCode(RspCode.SUCCESS.getCode());
        } catch (Exception e) {
            log.warn("invoke error", e);
            response.setCode(RspCode.UNKNOWN_SERVICE.getCode());
            response.setErrorMsg(RspCode.UNKNOWN_SERVICE.getMessage());
        }
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn(cause.getMessage());
        ctx.close();
    }
}
