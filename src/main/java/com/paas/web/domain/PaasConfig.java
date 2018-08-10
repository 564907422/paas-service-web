package com.paas.web.domain;


import javax.persistence.*;


@Entity
@Table(name = "paas_config")
public class PaasConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    private Byte type;

    private String servers;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers == null ? null : servers.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}