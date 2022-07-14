package com.sparrow.mason.core.netty.client;

import com.sparrow.mason.api.spi.Singleton;
import com.sparrow.mason.core.RpcTransport;
import com.sparrow.mason.core.TransportClient;
import com.sparrow.mason.core.netty.decoder.RpcRequestEncoder;
import com.sparrow.mason.core.netty.decoder.RpcResponseDecoder;
import com.sparrow.mason.core.netty.handler.RpcRequestHandler;
import com.sparrow.mason.core.transport.netty.NettyTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;

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
    public RpcTransport createTransport(InetSocketAddress address, long connectionTimeout) {
        return new NettyTransport(createChannel(address, connectionTimeout));
    }

    private Channel createChannel(InetSocketAddress address, long connectionTimeout) {
        if (Objects.isNull(address)) {
            throw new IllegalArgumentException("Address can not be null");
        }
        Channel channel = ChannelRegistry.getInstance().get(address);
        if (Objects.nonNull(channel)) {
            return channel;
        }
        ChannelFuture channelFuture = bootstrap.connect(address);
        channelFuture.addListener(future -> {
            if (future.await(connectionTimeout)) {
                throw new IllegalStateException("Connection timeout");
            }
        });
        channel = channelFuture.channel();
        if (Objects.isNull(channel) || !channel.isActive()) {
            throw new IllegalStateException("Channel unHealthy");
        }
        log.info("NettyClient connect to [{}] successfully", address);
        return channel;
    }

    private Bootstrap buildBoostrap(EventLoopGroup ioEventGroup, ChannelHandler channelHandler) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSctpChannel.class).group(ioEventGroup).handler(channelHandler).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        return bootstrap;
    }

    private ChannelHandler buildChannelHandler() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new RpcResponseDecoder()).addLast(new RpcRequestEncoder()).addLast(new RpcRequestHandler());
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