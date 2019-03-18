package com.paas.web.repository;

import com.paas.web.domain.PaasInstanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaasInstanceLogRepository extends JpaRepository<PaasInstanceLog, Integer> {
}
