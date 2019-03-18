package com.paas.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.paas.web.constants.ServiceConstants;
import com.paas.web.domain.PaasInstanceLog;
import com.paas.web.domain.PaasServiceInstance;
import com.paas.web.domain.PaasServiceResource;
import com.paas.web.domain.SysUser;
import com.paas.web.service.*;
import com.paas.web.utils.MD5Util;
import com.paas.web.vo.LoginVo;
import com.paas.web.vo.RspVo;
import com.paas.web.vo.ServiceUpdateVo;
import com.paas.web.vo.ServiceVo;
import com.paas.zk.zookeeper.ZKClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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
    @Resource
    IPaasInstanceLogService paasInstanceLogService;
    @Resource
    ISysUserService sysUserService;

    private ZKClient zkClient;

    @ResponseBody
    @RequestMapping(value = "/open", method = RequestMethod.POST, produces = "application/json")
    public RspVo open(@RequestBody ServiceVo serviceVo) {
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
            return RspVo.error(ServiceConstants.INFO.code_fail + "", ServiceConstants.OPEN_ERROR_STR);
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
                return RspVo.success(serviceId);
            }
            //2
            LOGGER.debug("2.--------获得serviceResource----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
            PaasServiceResource serviceResource = paasServiceResourceService.getPaasServiceResource(serviceType);
            if (serviceResource == null)
                return RspVo.error(ServiceConstants.INFO.code_fail + "", ServiceConstants.OPEN_ERROR_STR);
//            LOGGER.debug("2.1--------处理server----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
//            handleServer(buizCode, type, serviceId, serviceResource);
            LOGGER.debug("2.2--------添加zookeeper信息----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
            addzkConf(buizCode, type, serviceId, serviceResource);

            //3  addInstance();
            LOGGER.debug("3--------沉淀用户实例信息----buizCode:{}-----serviceId:{}-----", buizCode, serviceId);
            addInstance(buizCode, type, serviceId, remark, serviceResource);
        } catch (Exception e) {
            LOGGER.error("", e);
            return RspVo.error(ServiceConstants.INFO.code_fail + "", ServiceConstants.OPEN_ERROR_STR);

        }

        return RspVo.success(serviceId);

    }

    @ResponseBody
    @RequestMapping(value = "/tslist", method = RequestMethod.POST)
    public RspVo tsList(HttpServletRequest request, @RequestBody JSONObject param) {
        LOGGER.info("tslist 请求参数：{}", param.toJSONString());
        Page<PaasServiceInstance> page = paasServiceInstanceService.getInstanceListByCondition(
                param.getString("serviceId"), param.getString("note"),
                param.getInteger("start"), param.getInteger("pagesize"));

        JSONArray array = new JSONArray();

        List<PaasServiceInstance> list = page.getContent();

        for (PaasServiceInstance paasServiceInstance : list) {
            JSONObject jsonObject = new JSONObject();
            String[] s = paasServiceInstance.getServiceId().split(ServiceConstants.SPLIT_STR);
            jsonObject.put("type", s[0] == null ? "" : s[0]);
            jsonObject.put("servicerId", paasServiceInstance.getServiceId());
            jsonObject.put("remark", paasServiceInstance.getRemark());
            array.add(jsonObject);
        }

        JSONObject result = new JSONObject();
        result.put("list", array);
        result.put("totalPages", page.getTotalPages());
        result.put("totalEl", page.getTotalElements());
        result.put("curPage", param.getInteger("start"));
        result.put("pageSize", param.getInteger("pagesize"));

        return RspVo.success(result);
    }


    @ResponseBody
    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public RspVo loginout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        session.removeAttribute(ServiceConstants.SESSION_KEY);

        return RspVo.success("success");
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RspVo login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginVo vo) {
        if (StringUtils.isEmpty(vo.getUsername()) || StringUtils.isEmpty(vo.getPassword())) {
            return RspVo.error(ServiceConstants.INFO.code_fail + "", "参数有误,帐号密码必填");
        }

        SysUser userDetails = sysUserService.findByUsername(vo.getUsername());
        if (userDetails == null || !MD5Util.encode(vo.getPassword()).equals(userDetails.getPassword())) {
            return RspVo.error(ServiceConstants.INFO.code_fail + "", "登录失败,帐号密码有误");
        }

        HttpSession session = request.getSession();
        session.setAttribute(ServiceConstants.SESSION_KEY, userDetails);

        return RspVo.success(userDetails);
    }


    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public RspVo detail(HttpServletRequest request, String serviceId) {
        LOGGER.debug("/detail,请求参数，serviceId: {}", serviceId);
        if (StringUtils.isEmpty(serviceId)) {
            return RspVo.error(ServiceConstants.INFO.code_fail + "", "参数有误");
        }

        PaasServiceInstance paasServiceInstance = paasServiceInstanceService.findByServiceId(serviceId);
        Map<String, Map> resultmap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("servers", paasServiceInstance.getServers());
        map.put("serverInfo", StringUtils.isEmpty(paasServiceInstance.getServerInfo()) ? ""
                : JSONObject.parse(paasServiceInstance.getServerInfo()));
        map.put("conf", JSONObject.parse(paasServiceInstance.getClientConf()));
        resultmap.put("serverInfo", map);
        LOGGER.info("serviceInfo: {}", JSON.toJSON(resultmap));
        return RspVo.success(resultmap);
    }


    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RspVo update(HttpServletRequest request, @RequestBody ServiceUpdateVo param) throws Exception {
        LOGGER.debug("/update,请求参数，param: {}", JSON.toJSONString(param));
        String serviceId = param.getServiceId();
        if (StringUtils.isEmpty(serviceId) || StringUtils.isEmpty(param.getConfigInfo())) {
            return RspVo.error(ServiceConstants.INFO.code_fail + "", "参数有误");
        }

        PaasServiceInstance paasServiceInstance = paasServiceInstanceService.findByServiceId(serviceId);
        if (paasServiceInstance == null) {
            return RspVo.error(ServiceConstants.INFO.code_fail + "", "参数有误");
        }

        if (zkClient == null) {
            String zkUrl = paasConfigService.getInnerZk();
            zkClient = new ZKClient(zkUrl, ServiceConstants.ZK_TIMEOUT);
        }
        if (zkClient == null) {
            return RspVo.error(ServiceConstants.INFO.code_fail + "", "请检查zk");
        }

        String[] patharrs = serviceId.split("-");
        String path = ServiceConstants.ZK_PATH_PRE + ServiceConstants.ZK_PATH_SPLIT
                + patharrs[0] + ServiceConstants.ZK_PATH_SPLIT + patharrs[1]
                + ServiceConstants.ZK_PATH_SPLIT + serviceId;
        LOGGER.debug("组装path---------------->>{}", path);
        if (zkClient.exists(path)) {
            //修改zookeeper
            zkClient.setNodeData(path, param.getConfigInfo());
            LOGGER.debug("更新zk信息：path:{}", path);
        } else {
            zkClient.createNode(path, param.getConfigInfo());
            LOGGER.debug("创建zk信息：path:{}", path);
        }

        //记录更新前版本信息
        JSONObject snapshot = new JSONObject();
        snapshot.put("serverInfo", paasServiceInstance.getServerInfo());
        snapshot.put("servers", StringUtils.isEmpty(paasServiceInstance.getServers()) ? ""
                : JSONObject.parse(paasServiceInstance.getServers()));
        snapshot.put("conf", StringUtils.isEmpty(paasServiceInstance.getClientConf()) ? ""
                : JSONObject.parse(paasServiceInstance.getClientConf()));
        String oldSnapShot = snapshot.toString();
        //更新数据库信息
        JSONObject configJson = JSONObject.parseObject(param.getConfigInfo());
        paasServiceInstance.setServerInfo(configJson.getString("serverInfo"));
        paasServiceInstance.setServers(configJson.getString("servers"));
        paasServiceInstance.setClientConf(configJson.getString("conf"));
        paasServiceInstanceService.update(paasServiceInstance);

        //记录日志
        PaasInstanceLog log = new PaasInstanceLog();
        log.setBeforeVersion(oldSnapShot);
        log.setAfterVersion(configJson.toString());
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        SysUser user = (SysUser) request.getSession().getAttribute(ServiceConstants.SESSION_KEY);
        log.setUserId(user.getId());
        log.setServiceId(serviceId);
        paasInstanceLogService.insert(log);

        return RspVo.success("success");
    }


    //******************************* 辅助方法 ****************************************


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
            return getMqConfig(serviceResource, buizCode);
        }
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("servers", serviceResource.getServers());
        info.put("serverInfo", getServiceInfo4Map(serviceResource.getServerInfo(), type, buizCode, serviceId, serviceResource));

        info.put("conf", getClientConf4Map(type, buizCode, serviceId));

        return JSON.toJSONString(info);
    }

    // {"username":"bbtree","password":"hyww@1z3",
    // "port":5672,"host":"114.55.104.39",
    // "exchange":"","routings":[]}
    private String getMqConfig(PaasServiceResource serviceResource, String bizCode) {
        LOGGER.debug(" --> gernenate mq config for {}, server:{}, base:{}", bizCode,
                serviceResource.getServers(),
                serviceResource.getServerInfo());
        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(serviceResource.getServerInfo());
        String servers = serviceResource.getServers();
        if (!servers.contains(",")) {
            String[] ipPort = servers.split(":");
            map.put("host", ipPort[0]);
            map.put("port", ipPort[1]);
        } else {
            // TODO not support
            LOGGER.warn("---> can not support servers: {}", servers);
        }
        map.put("exchange", bizCode + "_ex_" + UUID.randomUUID().toString().replaceAll("-", ""));
        Map<String, String> rout = new HashMap<String, String>();
        rout.put("count", "1");
        rout.put("key", bizCode + "_key_" + UUID.randomUUID().toString().replaceAll("-", ""));
        rout.put("queue", bizCode + "_queue_" + UUID.randomUUID().toString().replaceAll("-", ""));
        ((List) map.get("routings")).add(rout);

        String resultJson = JSON.toJSONString(map);
        LOGGER.debug(" --> gernenate mq config: {}", resultJson);
        return resultJson;

//        return "{\"username\":\"bbtree\",\"password\":\"hyww@1z3\",\"port\":5672," +
//                "\"host\":\"114.55.104.39\"," +
//                "\"exchange\":\"bbtree_ad_pv_exchange\"," +
//                "\"routings\":[{\"count\":1,\"key\":\"ad_exposure_key\",\"queue\":\"bbtree_ad_pv_queue\"}]," +
//                "\"consumerThreads\":10}";
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
