package com.paas.web.service.impl;

import com.paas.web.domain.PaasServiceInstance;
import com.paas.web.repository.PaasServiceInstanceRepository;
import com.paas.web.service.IPaasServiceInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaasServiceInstanceServiceImpl implements IPaasServiceInstanceService {
    @Resource
    PaasServiceInstanceRepository paasServiceInstanceRepository;

    @Override
    public boolean existInstance(String serviceId) {
        PaasServiceInstance paasServiceInstance = paasServiceInstanceRepository.findByServiceId(serviceId);
        if (paasServiceInstance == null) {
            return false;
        }
        return true;
    }

    @Override
    public Integer insert(PaasServiceInstance paasServiceInstance) {
        return paasServiceInstanceRepository.save(paasServiceInstance).getId();
    }
}
