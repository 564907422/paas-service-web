package com.paas.web.service.impl;

import com.paas.web.domain.PaasConfig;
import com.paas.web.repository.PaasConfigRepository;
import com.paas.web.service.IPaasConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PaasConfigServiceImpl implements IPaasConfigService {
    @Autowired
    private PaasConfigRepository paasConfigRepository;

    @Override
    public PaasConfig findPaasConfigByStatusAndType(Integer status, Integer type) {
        return paasConfigRepository.findPaasConfigByStatusAndType(status.toString(), Byte.parseByte(type.toString()));
    }

    @Override
    public String getInnerZk() {
        PaasConfig paasConfig = findPaasConfigByStatusAndType(1,1);
        if(paasConfig!=null && !StringUtils.isEmpty(paasConfig.getServers()))
        {
            return paasConfig.getServers();
        }
        return "";
    }
}
