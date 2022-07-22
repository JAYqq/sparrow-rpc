package com.sparrow.mason.core.netty.client;

import com.sparrow.mason.api.spi.Singleton;
import com.sparrow.mason.core.RpcTransport;
import com.sparrow.mason.core.TransportClient;
import com.sparrow.mason.core.netty.decoder.RpcCommandEncoder;
import com.sparrow.mason.core.netty.decoder.RpcResponseDecoder;
import com.sparrow.mason.core.netty.handler.RpcResponseHandler;
import com.sparrow.mason.core.transport.netty.NettyTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author chengwei_shen
 * @date 2022/7/14 10:50
 **/
@Singleton
@Slf4j
public class NettyClient implements TransportClient {
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    public NettyClient() {
        this.eventLoopGroup = buildEventGroup();
        this.bootstrap = buildBoostrap(this.eventLoopGroup, buildChannelHandler());
    }

    @Override
    public RpcTransport createTransport(InetSocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        return new NettyTransport(createChannel(address, connectionTimeout));
    }

    private Channel createChannel(InetSocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        if (Objects.isNull(address)) {
            throw new IllegalArgumentException("Address can not be null");
        }
        Channel channel = ChannelRegistry.getInstance().get(address);
        if (Objects.nonNull(channel)) {
            return channel;
        }
        ChannelFuture channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout)) {
            throw new TimeoutException();
        }
        channel = channelFuture.channel();
        if (Objects.isNull(channel) || !channel.isActive()) {
            throw new IllegalStateException("Channel unHealthy");
        }
        log.info("NettyClient connect to [{}] successfully", address);
        return channel;
    }

    private Bootstrap buildBoostrap(EventLoopGroup ioEventGroup, ChannelHandler channelHandler) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class).group(ioEventGroup).handler(channelHandler).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        return bootstrap;
    }

    private ChannelHandler buildChannelHandler() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                        .addLast(new RpcResponseDecoder())
                        .addLast(new RpcCommandEncoder())
                        .addLast(new RpcResponseHandler());
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
    public void close() throws IOException {
        ChannelRegistry.getInstance().removeAll();
        eventLoopGroup.shutdownGracefully();
    }
}
