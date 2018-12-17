package com.eureka.client.model.entity;

/**
 * @author techoneduan
 * @date 2018/12/17
 */
public class NettyEntity {

    private String header;

    private Object params;

    public String getHeader () {
        return header;
    }

    public void setHeader (String header) {
        this.header = header;
    }

    public Object getParams () {
        return params;
    }

    public void setParams (Object params) {
        this.params = params;
    }
}
