package com.paas.web.repository;

import com.paas.web.domain.PaasConfig;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

public interface PaasConfigRepository extends PagingAndSortingRepository<PaasConfig,Integer> {
    /**
     * 根据状态和类型查找数据
     * @param status
     * @param type
     * @return
     */
    PaasConfig findPaasConfigByStatusAndType(Integer status,Integer type);
}
