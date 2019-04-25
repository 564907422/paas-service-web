package com.paas.web.repository;

import com.paas.web.domain.PaasServiceResource;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaasServiceResourceRepository extends PagingAndSortingRepository<PaasServiceResource, Integer> {
    PaasServiceResource findByServiceTypeAndStatusAAndEnv(Byte type, String status, String env);
}
