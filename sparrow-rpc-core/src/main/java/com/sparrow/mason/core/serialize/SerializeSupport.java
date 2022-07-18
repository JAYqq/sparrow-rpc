package com.sparrow.mason.core.serialize;

import com.sparrow.mason.api.spi.SpiSupport;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chengwei_shen
 * @date 2022/7/15 14:39
 **/
public class SerializeSupport {
    private static Map<Byte, Class<?>> typeClassMap = new ConcurrentHashMap<>();
    private static Map<Class<?>, Serializer<?>> classSerializerMap = new ConcurrentHashMap<>();

    static {
        //加载所有序列器
        Collection<Serializer> serializers = SpiSupport.loadAll(Serializer.class);
        serializers.forEach(serializer -> {
            typeClassMap.put(serializer.getType(), serializer.getSerializerClass());
            classSerializerMap.put(serializer.getSerializerClass(), serializer);
        });
    }

    /**
     * 默认序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T t) {
        Serializer<T> serializer = (Serializer<T>) classSerializerMap.get(t.getClass());
        if (Objects.isNull(serializer)) {
            throw new IllegalArgumentException(String.format("Cannot find correct serializer for class:%s", t.getClass()));
        }
        ByteBuffer buffer = ByteBuffer.allocate(serializer.getSize(t) + 1);
        //先放上类型
        buffer.put(serializer.getType());
        return serializer.serialize(t, buffer);
    }

    /**
     * 指定clazz序列化
     *
     * @param t
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T t, Class clazz) {
        Serializer<T> serializer = (Serializer<T>) classSerializerMap.get(clazz);
        if (Objects.isNull(serializer)) {
            throw new IllegalArgumentException(String.format("Cannot find correct serializer for class:%s", t.getClass()));
        }
        ByteBuffer buffer = ByteBuffer.allocate(serializer.getSize(t) + 1);
        //先放上类型
        buffer.put(serializer.getType());
        return serializer.serialize(t, buffer);
    }

    public static <E> E parse(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byte type = buffer.get();
        Class<?> clazz = typeClassMap.get(type);
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException(String.format("Unknown type:%s", type));
        }
        Serializer<?> serializer = classSerializerMap.get(clazz);
        Object rs = serializer.parse(buffer);
        return (E) rs;
    }
}
