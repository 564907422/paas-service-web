package com.paas.web.repository;

import com.paas.web.domain.PaasServiceInstance;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaasServiceInstanceRepository extends PagingAndSortingRepository<PaasServiceInstance,Integer> {
    PaasServiceInstance findByServiceId(String serviceId);
}
