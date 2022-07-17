package com.sparrow.mason.core.client;

/**
 * @author chengweishen
 * @date 2022/7/17 11:29
 */
public enum CommandTypes {

    RPC_REQUEST("RPC_REQUEST");

    CommandTypes(String type) {
        this.type = type;
    }

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
