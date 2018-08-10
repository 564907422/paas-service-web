package com.paas.web.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "paas_service_instance")
public class PaasServiceInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_id")
    private String serviceId;

    private String servers;

    @Column(name = "server_info")
    private String serverInfo;

    @Column(name = "client_conf")
    private String clientConf;

    private String status;

    @Column(name = "begin_time")
    private Timestamp beginTime;

    @Column(name = "end_time")
    private Timestamp endTime;
    
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
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

    public String getClientConf() {
        return clientConf;
    }

    public void setClientConf(String clientConf) {
        this.clientConf = clientConf == null ? null : clientConf.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}