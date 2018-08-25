package com.paas.web.service;

import com.paas.web.domain.PaasServiceInstance;

public interface IPaasServiceInstanceService {
    boolean existInstance(String serviceId);

    Integer insert(PaasServiceInstance paasServiceInstance);
}
