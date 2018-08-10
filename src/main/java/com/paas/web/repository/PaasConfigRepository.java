package com.paas.web.repository;

import com.paas.web.domain.PaasConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaasConfigRepository extends PagingAndSortingRepository<PaasConfig,Integer> {
}
