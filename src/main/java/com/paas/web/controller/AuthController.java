package com.paas.web.controller;

import com.paas.web.service.IPaasConfigService;
import com.paas.web.vo.AuthResultVo;
import com.paas.zk.zookeeper.ZKClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/paas/auth")
public class AuthController {

    private ZKClient zkClient;
    private String zkUrl = "";
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IPaasConfigService paasConfigService;

    @ResponseBody
    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public AuthResultVo service(String serviceId) {
        logger.debug("--------/auth/service--{}---------", serviceId);
        AuthResultVo vo = authService(serviceId);
        return vo;
    }


    private AuthResultVo authService(String serviceId) {
        AuthResultVo vo = new AuthResultVo();
        if (StringUtils.isEmpty(serviceId))
            return vo;
        String[] serviceInfo = serviceId.split("-");
        if (serviceInfo.length != 3)
            return vo;
        try {
            String path = "/bbtree/ts/" + serviceInfo[0] + "/" + serviceInfo[1] + "/" + serviceId;
            logger.debug("--------/auth/service-path={}---------", path);

            //String zkUrl = "";
            if (zkClient == null) {
                zkUrl = paasConfigService.findPaasConfigByStatusAndType(1, 1).getServers();
                logger.debug("--------/auth/service-zkUrl={}---------", zkUrl);
                if (StringUtils.isEmpty(zkUrl)) {
                    vo.setMsg("inner zk error.");
                    return vo;
                }
                zkClient = new ZKClient(zkUrl, 3000);
            }
            //zookeeper上服务节点存在为正常
            if (zkClient.exists(path)) {
                logger.info("serviceId:{} , values: {}", serviceId, zkClient.getNodeData(path));
                vo.setServiceId(serviceId);
                vo.setZkAdress(zkUrl);
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return vo;
    }
}
