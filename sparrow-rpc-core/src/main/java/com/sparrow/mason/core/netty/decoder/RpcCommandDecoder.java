package com.sparrow.mason.core.netty.decoder;

import com.sparrow.mason.core.netty.dto.RpcCommand;
import com.sparrow.mason.core.netty.dto.RpcHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author chengwei_shen
 * @date 2022/7/14 11:42
 **/
public class RpcCommandDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readInt();
        byte[] bytes = new byte[len];
        byteBuf.writeBytes(bytes);
        RpcHeader header = deHeader(bytes);
        len = byteBuf.readInt();
        bytes = new byte[len];
        byteBuf.writeBytes(byteBuf);
        RpcCommand command = new RpcCommand();
        command.setHeader(header);
        command.setData(bytes);
        list.add(command);
    }

    private RpcHeader deHeader(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int len = byteBuffer.getInt();
        byte[] arrBytes = new byte[len];
        byteBuffer.get(arrBytes);
        String version = new String(arrBytes, StandardCharsets.UTF_8);

        len = byteBuffer.getInt();
        arrBytes = new byte[len];
        byteBuffer.get(arrBytes);
        String traceId = new String(arrBytes, StandardCharsets.UTF_8);

        len = byteBuffer.getInt();
        arrBytes = new byte[len];
        byteBuffer.get(arrBytes);
        String type = new String(arrBytes, StandardCharsets.UTF_8);

        RpcHeader header = new RpcHeader();
        header.setVersion(version);
        header.setTraceId(traceId);
        header.setType(type);
        return header;
    }
}
