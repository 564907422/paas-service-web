package com.paas.web.service;

import com.paas.web.domain.PaasConfig;
import com.paas.web.repository.PaasConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaasConfigServiceImpl implements IPaasConfigService {
    @Autowired
    private PaasConfigRepository paasConfigRepository;

    @Override
    public PaasConfig findPaasConfigByStatusAndType(Integer status, Integer type) {
        return paasConfigRepository.findPaasConfigByStatusAndType(status, type);
    }
}
