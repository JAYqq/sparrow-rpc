package com.sparrow.mason.core.netty.decoder;

import com.sparrow.mason.core.netty.dto.RpcResponse;
import com.sparrow.mason.core.serialize.SerializeSupport;
import com.sparrow.mason.core.serialize.SerializerType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author chengwei_shen
 * @date 2022/7/14 11:42
 **/
public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse, ByteBuf byteBuf) throws Exception {
        byte[] bytes = SerializeSupport.serialize(rpcResponse, SerializerType.HESSIAN.getType());
        byteBuf.writeBytes(bytes);
    }
}
