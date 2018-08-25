package com.paas.web.service;

import com.paas.web.domain.PaasConfig;

public interface IPaasConfigService {
    PaasConfig findPaasConfigByStatusAndType(Integer status, Integer type);

    String getInnerZk();
}
