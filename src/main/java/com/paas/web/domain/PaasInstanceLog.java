package com.paas.web.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "paas_instance_log")
public class PaasInstanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "before_version")
    private String beforeVersion;
    @Column(name = "after_version")
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

    public String getBeforeVersion() {
        return beforeVersion;
    }

    public void setBeforeVersion(String beforeVersion) {
        this.beforeVersion = beforeVersion;
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
