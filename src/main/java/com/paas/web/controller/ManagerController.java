package com.paas.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paas.web.Constants.ServiceConstants;
import com.paas.web.domain.PaasServiceInstance;
import com.paas.web.domain.PaasServiceResource;
import com.paas.web.service.IPaasConfigService;
import com.paas.web.service.IPaasServiceInstanceService;
import com.paas.web.service.IPaasServiceResourceService;
import com.paas.web.vo.ServiceVo;
import com.paas.zk.zookeeper.ZKClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/paas/manager/")
public class ManagerController {
    private static Logger LOGGER = LoggerFactory.getLogger(ManagerController.class);

    @Resource
    IPaasServiceInstanceService paasServiceInstanceService;
    @Resource
    IPaasServiceResourceService paasServiceResourceService;
    @Resource
    IPaasConfigService paasConfigService;

    private ZKClient zkClient;

    @ResponseBody
    @RequestMapping(value = "/open")
    public String open(ServiceVo serviceVo) {
        LOGGER.debug("--------/open--{},{},{}---------",
                serviceVo.getBuizCode(),
                serviceVo.getType(),
                serviceVo.getRemark());

        Byte serviceType = serviceVo.getType();
        String buizCode = serviceVo.getBuizCode();
        String remark = serviceVo.getRemark();
        //1
        LOGGER.debug("1.--------获得serviceId----buizCode:{}--serviceType:{}-----", buizCode, serviceType);
        if (!validate(buizCode, serviceType)) {
            LOGGER.error("1.1----非法参数--buizCode:{}--serviceType:{}----", buizCode, serviceType);
            return ServiceConstants.OPEN_ERROR_STR;
        }
        /**serviceType  99时，buizCode是已经拼好的serviceId*/
        String type = getType(serviceType, buizCode);
        String serviceId = getServiceId(buizCode, type, serviceType);
        buizCode = getBuizCode(serviceType, buizCode);
        serviceType = getServiceType(type, serviceType);
        LOGGER.debug("1.2--------获得serviceId----buizCode:{}--serviceType:{}-----", buizCode, serviceType);

        try {
            //校验服务是否已经存在
            if (paasServiceInstanceService.existInstance(serviceId)) {
                LOGGER.warn("1.3--------serviceId:{} already exist-------", serviceId);
                return serviceId;
            }
            //2
            LOGGER.debug("2.--------获得serviceResource----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
            PaasServiceResource serviceResource = paasServiceResourceService.getPaasServiceResource(serviceType);
            if (serviceResource == null)
                return ServiceConstants.OPEN_ERROR_STR;
//            LOGGER.debug("2.1--------处理server----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
//            handleServer(buizCode, type, serviceId, serviceResource);
            LOGGER.debug("2.2--------添加zookeeper信息----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
            addzkConf(buizCode, type, serviceId, serviceResource);

            //3  addInstance();
            LOGGER.debug("3--------沉淀用户实例信息----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
            addInstance(buizCode, type, serviceId, remark, serviceResource);
        } catch (Exception e) {
            LOGGER.error("", e);
            return ServiceConstants.OPEN_ERROR_STR;

        }

        return serviceId;

    }


    private boolean validate(String buizCode, Byte serviceType) {
        if (StringUtils.isEmpty(buizCode))
            return false;
        if (serviceType == null)
            return false;
        return true;
    }

    private String getType(Byte cacheType, String buizCode) {
        switch (cacheType) {
            case 1:
                return ServiceConstants.CACHE_STR;
            case 2:
                return ServiceConstants.MQ_STR;
            case 3:
                return ServiceConstants.MONGO_STR;
            case 99:
                String[] input = buizCode.split("-");
                return input[1];
        }

        return null;
    }

    private String getServiceId(String buizCode, String type, Byte serviceType) {
        if (serviceType.intValue() == 99)
            return buizCode;

        return buizCode + ServiceConstants.SPLIT_STR + type
                + ServiceConstants.SPLIT_STR
                + UUID.randomUUID().toString().replaceAll("-", "");
    }


    private String getBuizCode(Byte serviceType, String buizCode) {
        if (serviceType == 99) {
            String[] input = buizCode.split("-");
            return input[0];
        } else
            return buizCode;
    }

    private Byte getServiceType(String type, Byte serviceType) {
        if (serviceType == 99) {
            if (ServiceConstants.CACHE_STR.equals(type))
                return 1;
            if (ServiceConstants.MQ_STR.equals(type))
                return 2;
            if (ServiceConstants.MONGO_STR.equals(type))
                return 3;
            return 0;
        } else
            return serviceType;
    }


    private void addzkConf(String buizCode, String type, String serviceId,
                           PaasServiceResource serviceResource) throws Exception {
        if (zkClient == null) {
            String zkUrl = paasConfigService.getInnerZk();
            zkClient = new ZKClient(zkUrl, ServiceConstants.ZK_TIMEOUT);
        }
        if (zkClient == null) {
            throw new Exception("检查zk server");
        }
        String path = ServiceConstants.ZK_PATH_PRE + ServiceConstants.ZK_PATH_SPLIT
                + buizCode + ServiceConstants.ZK_PATH_SPLIT + type
                + ServiceConstants.ZK_PATH_SPLIT + serviceId;
        String value = getInfoValue(serviceResource, type, buizCode, serviceId);
        LOGGER.debug("-----------conf value:{}--------", value);
        if (!zkClient.exists(path))
            zkClient.createNode(path, value);
    }


    private String getInfoValue(PaasServiceResource serviceResource, String type,
                                String buizCode, String serviceId) {
        if ("mq".equals(type)) {
//            return getMqConfig(serviceResource, buizCode);
        }
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("servers", serviceResource.getServers());
        info.put("serverInfo", getServiceInfo4Map(serviceResource.getServerInfo(), type, buizCode, serviceId, serviceResource));

        info.put("conf", getClientConf4Map(type, buizCode, serviceId));

        return JSON.toJSONString(info);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map getClientConf4Map(String type, String buizCode, String serviceId) {
        Map conf = new HashMap();
        if (ServiceConstants.CACHE_STR.equals(type)) {
            conf.put("maxActive", 1024);
            conf.put("maxIdle", 100);
            conf.put("maxWait", 3000);
            conf.put("testOnBorrow", "true");
            conf.put("testOnReturn", "false");
        } else if (ServiceConstants.MONGO_STR.equals(type)) {
            conf.put("threshold", 1000);
        }
        return conf;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map getServiceInfo4Map(String serverInfo, String type, String buizCode, String serviceId,
                                   PaasServiceResource serviceResource) {
        if (ServiceConstants.MONGO_STR.equals(type)) {
            Map conf = new HashMap();
            conf.put("database", getDbName(buizCode, serviceId));
            conf.put("userName", getUserName4Zk(buizCode, serviceId, serverInfo));
            conf.put("password", getPassword4ZK(buizCode, serviceId, serverInfo));
            conf.put("bucket", "col" + getCollectionName(buizCode, serviceId));
            //开通mongo collection时，信息入库
            serviceResource.setServerInfo(JSON.toJSONString(conf));
            return conf;
        } else {
            if (!StringUtils.isEmpty(serverInfo))
                return (Map) JSONObject.parse(serverInfo);
        }
        return new HashMap();
    }


    private String getDbName(String buizCode, String serviceId) {
        return buizCode;
    }

    private String getUserName4Zk(String buizCode, String serviceId, String serverInfo) {
        if (!StringUtils.isEmpty(serverInfo)) {
            HashMap info = JSONObject.parseObject(serverInfo, HashMap.class);
            return info.get("userName") == null ? getUserName(buizCode, serviceId) : info.get("userName").toString();
        } else {
            return getUserName(buizCode, serviceId);
        }
    }

    private String getPassword4ZK(String buizCode, String serviceId, String serverInfo) {
        if (!StringUtils.isEmpty(serverInfo)) {
            HashMap info = JSONObject.parseObject(serverInfo, HashMap.class);
            return info.get("password") == null ? getPassword(buizCode, serviceId) : info.get("password").toString();
        } else {
            return getPassword(buizCode, serviceId);
        }
    }


    private String getCollectionName(String buizCode, String serviceId) {
        String[] str = serviceId.split(ServiceConstants.SPLIT_STR);
        return str[str.length - 1];
    }

    private String getUserName(String buizCode, String serviceId) {
        String name = getCollectionName(buizCode, serviceId);
        return name.substring(0, 8);
    }

    private String getPassword(String buizCode, String serviceId) {
        String name = getCollectionName(buizCode, serviceId);
        return name.substring(name.length() - 6);
    }


    private void addInstance(String buizCode, String type, String serviceId, String remark,
                             PaasServiceResource serviceResource) {
        PaasServiceInstance serviceInstance = new PaasServiceInstance();
        serviceInstance.setStatus(ServiceConstants.STATUS_VA);
        serviceInstance.setServers(serviceResource.getServers());
        serviceInstance.setServerInfo(serviceResource.getServerInfo());
        serviceInstance.setClientConf(getClientConf(type));
        serviceInstance.setServiceId(serviceId);
        serviceInstance.setBeginTime(new Timestamp(System.currentTimeMillis()));
        serviceInstance.setRemark(remark);
        paasServiceInstanceService.insert(serviceInstance);
    }


    private String getClientConf(String type) {
        if (ServiceConstants.CACHE_STR.equals(type))
            return ServiceConstants.CACHE_CLIENT_INFO;
        if (ServiceConstants.MONGO_STR.equals(type))
            return ServiceConstants.MONGO_CLIENT_INFO;
        return null;
    }

}
