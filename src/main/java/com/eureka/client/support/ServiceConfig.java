package com.eureka.client.support;


/**
 * @author techoneduan
 * @date 2018/12/14
 */
public class ServiceConfig {

    private String name;

    private String mapping;

    private String method = RestMethod.GET;//POST GET

    private String returnValue;

    private String param;

    private String description;

    public void setMapping(String map) {
        this.mapping = map;
    }

    public String getMapping() {
        return this.mapping;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    public void setName(String na) {
        this.name = na;
    }

    public String getName() {
        return this.name;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnValue() {
        return this.returnValue;
    }

    public void setParam(String para) {
        this.param = para;
    }

    public String getParam() {
        return this.param;
    }

    public void setDescription(String descrip) {
        this.description = descrip;
    }

    public String getDescription() {
        return this.description;
    }

}
