package com.paas.web.service;

import com.paas.web.domain.PaasServiceResource;

public interface IPaasServiceResourceService {
    PaasServiceResource getPaasServiceResource(Byte serviceType);
}
