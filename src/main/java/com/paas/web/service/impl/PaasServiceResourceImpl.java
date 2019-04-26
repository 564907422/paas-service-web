package com.paas.web.service.impl;

import com.paas.web.domain.PaasServiceResource;
import com.paas.web.repository.PaasServiceResourceRepository;
import com.paas.web.service.IPaasServiceResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaasServiceResourceImpl implements IPaasServiceResourceService {
    @Resource
    PaasServiceResourceRepository paasServiceResourceRepository;

    @Override
    public PaasServiceResource getPaasServiceResource(Byte serviceType, String env) {
        return paasServiceResourceRepository.findByServiceTypeAndStatusAndEnv(serviceType, "1", env);
    }
}
