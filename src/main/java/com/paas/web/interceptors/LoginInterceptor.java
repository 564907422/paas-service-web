package com.paas.web.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.paas.web.constants.ServiceConstants;
import com.paas.web.vo.RspVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        LOGGER.info("session_id:"+session.getId());
        if (session.getAttribute(ServiceConstants.SESSION_KEY) != null) {
            return true;
        }

        PrintWriter writer = null;
        try {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("utf-8");
//            response.setStatus(401);
            response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With,X_Requested_With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            writer = response.getWriter();
            String s = JSONObject.toJSONString(RspVo.error(ServiceConstants.INFO.code_fail + "", "登陆超时或者尚未登陆, 请登陆!"));
            writer.write(s);
            writer.flush();
        } catch (Exception e) {
            LOGGER.error("该错误可以忽略", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return false;
    }
}
