package com.eureka.client.model.eureka;

import java.util.List;

/**
 * @author techoneduan
 * @date 2018/12/19
 */
public class RegistryInfo {

    private String clientName;

    private String ipaddr;

    private String port;

    private List<ServiceConfig> serviceConfigs;

    public String getClientName () {
        return clientName;
    }

    public void setClientName (String clientName) {
        this.clientName = clientName;
    }

    public String getIpaddr () {
        return ipaddr;
    }

    public void setIpaddr (String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getPort () {
        return port;
    }

    public void setPort (String port) {
        this.port = port;
    }

    public List<ServiceConfig> getServiceConfigs () {
        return serviceConfigs;
    }

    public void setServiceConfigs (List<ServiceConfig> serviceConfigs) {
        this.serviceConfigs = serviceConfigs;
    }
}
