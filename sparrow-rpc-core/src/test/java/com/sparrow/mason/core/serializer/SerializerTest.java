package com.sparrow.mason.core.serializer;

import com.sparrow.mason.core.netty.dto.RpcHeader;
import com.sparrow.mason.core.netty.dto.RpcResponse;
import com.sparrow.mason.core.serialize.SerializeSupport;
import com.sparrow.mason.core.serialize.SerializerType;
import org.junit.Test;

import java.util.Objects;

/**
 * @author chengwei_shen
 * @date 2022/7/19 20:08
 **/
public class SerializerTest {
    @Test
    public void hessianTest() {
        RpcResponse response = new RpcResponse();
        RpcHeader header = new RpcHeader();
        header.setVersion("v1.0");
        header.setTraceId("11111");
        header.setType("request");
        response.setHeader(header);
        response.setCode(1);
        response.setErrorMsg("hahahahh");
        byte[] serialize = SerializeSupport.serialize(response, SerializerType.HESSIAN.getType());
        RpcResponse res = SerializeSupport.parse(serialize, SerializerType.HESSIAN.getType());
        assert Objects.nonNull(res);
    }
}
