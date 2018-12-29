package com.paas.web.service;

import com.paas.web.domain.PaasServiceInstance;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPaasServiceInstanceService {
    boolean existInstance(String serviceId);

    Integer insert(PaasServiceInstance paasServiceInstance);

    List<PaasServiceInstance> getInstanceList();

    Page<PaasServiceInstance> getInstanceListByCondition(String serviceId, String note, Integer start, Integer pageSize);

    PaasServiceInstance findByServiceId(String serviceId);
}
