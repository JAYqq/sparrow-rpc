package com.sparrow.mason.namesrv.dto;

import java.net.URI;

/**
 * @author chengwei_shen
 * @date 2022/7/20 13:53
 **/
public class MetaInfo {
    String serviceSign;
    URI uri;

    public String getServiceSign() {
        return serviceSign;
    }

    public void setServiceSign(String serviceSign) {
        this.serviceSign = serviceSign;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object obj) {
        MetaInfo t = (MetaInfo) obj;
        return t.getServiceSign().equals(this.getServiceSign()) && t.getUri().equals(this.getUri());
    }
}
