package com.paas.web.service.impl;

import com.paas.web.domain.PaasInstanceLog;
import com.paas.web.repository.PaasInstanceLogRepository;
import com.paas.web.service.IPaasInstanceLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaasInstanceLogServiceImpl implements IPaasInstanceLogService {
    @Resource
    private PaasInstanceLogRepository paasInstanceLogRepository;

    @Override
    public Integer insert(PaasInstanceLog log) {
        return paasInstanceLogRepository.save(log).getId();
    }
}
