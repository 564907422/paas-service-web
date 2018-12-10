package com.paas.web.repository;

import com.paas.web.domain.PaasServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaasServiceInstanceRepository extends JpaRepository<PaasServiceInstance,Integer> {
    PaasServiceInstance findByServiceId(String serviceId);
}
