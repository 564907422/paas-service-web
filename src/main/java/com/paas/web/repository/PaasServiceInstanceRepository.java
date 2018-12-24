package com.paas.web.repository;

import com.paas.web.domain.PaasServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaasServiceInstanceRepository extends JpaRepository<PaasServiceInstance, Integer>, JpaSpecificationExecutor {
    PaasServiceInstance findByServiceId(String serviceId);
}
