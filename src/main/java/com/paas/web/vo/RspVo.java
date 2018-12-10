package com.paas.web.vo;

import com.paas.web.constants.ServiceConstants;

import java.io.Serializable;

public class RspVo implements Serializable {
    private static final long serialVersionUID = 2800508019052248524L;
    private String code;
    private String msg;
    private Object data;

    public RspVo() {
    }

    public RspVo(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RspVo success(Object data) {
        return new RspVo(ServiceConstants.INFO.code_success + "", null, data);
    }

    public static RspVo error(String code, String msg) {
        return new RspVo(code, msg, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
