package com.paas.web.service;

import com.paas.web.domain.PaasServiceInstance;

import java.util.List;

public interface IPaasServiceInstanceService {
    boolean existInstance(String serviceId);

    Integer insert(PaasServiceInstance paasServiceInstance);

    Iterable<PaasServiceInstance>  getInstanceList();
}
