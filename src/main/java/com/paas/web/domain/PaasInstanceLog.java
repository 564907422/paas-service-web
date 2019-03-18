package com.paas.web.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "paas_instance_log")
public class PaasInstanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String beforeVesion;
    private String afterVersion;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "user_id")
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBeforeVesion() {
        return beforeVesion;
    }

    public void setBeforeVesion(String beforeVesion) {
        this.beforeVesion = beforeVesion;
    }

    public String getAfterVersion() {
        return afterVersion;
    }

    public void setAfterVersion(String afterVersion) {
        this.afterVersion = afterVersion;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
