package com.sparrow.mason.core.netty.decoder;

import com.sparrow.mason.core.serialize.SerializeSupport;
import com.sparrow.mason.core.serialize.SerializerType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 字节流从channel出来时需要转换成对象
 *
 * @author chengwei_shen
 * @date 2022/7/14 11:25
 **/
public class RpcResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object parse = SerializeSupport.parse(byteBuf.array(), SerializerType.HESSIAN.getType());
        list.add(parse);
    }
}
