package com.paas.web.vo;

import java.io.Serializable;


public class ServiceVo implements Serializable{
    /**
     * 业务编码
     */
    private String buizCode;
    /**
     * 服务类型  1是缓存  2MQ
     */
    private Byte type;
    /**
     * 描述
     */
    private String  remark;

    public String getBuizCode() {
        return buizCode;
    }

    public void setBuizCode(String buizCode) {
        this.buizCode = buizCode;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
