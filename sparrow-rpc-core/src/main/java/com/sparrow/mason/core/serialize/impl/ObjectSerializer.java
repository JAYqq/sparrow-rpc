package com.sparrow.mason.core.serialize.impl;

import com.sparrow.mason.core.serialize.Serializer;
import com.sparrow.mason.core.serialize.SerializerType;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @author chengwei_shen
 * @date 2022/7/11 15:05
 **/
public class ObjectSerializer implements Serializer<Object> {

    @Override
    public int getSize(Object o) {
        return toByteArray(o).length;
    }

    @Override
    public byte[] serialize(Object entry, ByteBuffer buffer) {
        byte[] bytes = toByteArray(entry);
        buffer.put(bytes);
        return buffer.array();
    }

    @Override
    public Object parse(ByteBuffer buffer) {
        return toObject(buffer.array());
    }

    @Override
    public byte getType() {
        return SerializerType.TYPE_OBJECT.getType();
    }

    @Override
    public Class<Object> getSerializerClass() {
        return Object.class;
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeByte(getType());
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            //先读取
            ois.readByte();
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

}
