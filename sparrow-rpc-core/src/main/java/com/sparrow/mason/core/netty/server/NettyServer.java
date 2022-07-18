package com.sparrow.mason.core.netty.server;

import com.sparrow.mason.core.TransportServer;
import com.sparrow.mason.core.netty.decoder.RpcCommandDecoder;
import com.sparrow.mason.core.netty.decoder.RpcResponseEncoder;
import com.sparrow.mason.core.netty.handler.RpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author chengwei_shen
 * @date 2022/7/18 17:47
 **/
public class NettyServer implements TransportServer {
    private final int port = 9999;
    private EventLoopGroup acceptEventGroup;
    private EventLoopGroup workerEventGroup;

    @Override
    public void start() {
        //专门处理连接的group，相当于多路复用的监听线程
        acceptEventGroup = buildEventGroup();
        //处理io事件的worker
        workerEventGroup = buildEventGroup();
        ChannelHandler channelHandler = buildChannelHandler();
        ServerBootstrap serverBootstrap = buildBoostrap(acceptEventGroup, workerEventGroup, channelHandler);
    }

    private ServerBootstrap buildBoostrap(EventLoopGroup acceptLoopGroup, EventLoopGroup workerEventGroup, ChannelHandler channelHandler) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .group(acceptLoopGroup, workerEventGroup)
                .childHandler(channelHandler)
                //是否开启Nagle算法，该算法会缓存网络数据包，相当于批处理（适合高流量，不适合实时性高场景）
                .childOption(ChannelOption.TCP_NODELAY, true)
                //是否连接保活，可以复用连接，减少建立连接开销
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return serverBootstrap;
    }

    private ChannelHandler buildChannelHandler() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast(new RpcCommandDecoder())
                        .addLast(new RpcResponseEncoder())
                        .addLast(new RpcRequestHandler());
            }
        };
    }

    private EventLoopGroup buildEventGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    @Override
    public void close() {

    }
}
