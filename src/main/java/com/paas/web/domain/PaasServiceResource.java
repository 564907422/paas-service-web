package com.paas.web.domain;

import javax.persistence.*;

@Entity
@Table(name = "paas_service_resource")
public class PaasServiceResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_type")
    private Byte serviceType;

    private String servers;

    @Column(name = "server_info")
    private String serverInfo;

    private String status;

    private String env;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers == null ? null : servers.trim();
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo == null ? null : serverInfo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}