package com.paas.web.handler;

import com.paas.web.constants.ServiceConstants;
import com.paas.web.vo.RspVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MyExceptionHandler {

    public static Logger LOGGER = LoggerFactory.getLogger(MyExceptionHandler.class);

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RspVo errorHandler(Exception ex, HttpServletRequest request) {
        LOGGER.error("接口请求报错，url：" + request.getRequestURI() + " and with params：" + request.getQueryString(), ex);
        return RspVo.error(ServiceConstants.INFO.code_fail + "", "服务器异常");
    }


}
