package com.paas.web.service.impl;

import com.paas.web.domain.PaasServiceInstance;
import com.paas.web.repository.PaasServiceInstanceRepository;
import com.paas.web.service.IPaasServiceInstanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaasServiceInstanceServiceImpl implements IPaasServiceInstanceService {
    @Resource
    PaasServiceInstanceRepository paasServiceInstanceRepository;

    @Override
    public boolean existInstance(String serviceId) {
        PaasServiceInstance paasServiceInstance = paasServiceInstanceRepository.findByServiceId(serviceId);
        if (paasServiceInstance == null) {
            return false;
        }
        return true;
    }

    @Override
    public Integer insert(PaasServiceInstance paasServiceInstance) {
        return paasServiceInstanceRepository.save(paasServiceInstance).getId();
    }

    @Override
    public List<PaasServiceInstance> getInstanceList() {
        List<PaasServiceInstance> all = paasServiceInstanceRepository.findAll();
        return all;
    }

    @Override
    public Page<PaasServiceInstance> getInstanceListByCondition(String serviceId, String note, Integer start, Integer pageSize) {
        Specification querySpecifi = new Specification<PaasServiceInstance>() {
            @Override
            public Predicate toPredicate(Root<PaasServiceInstance> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(serviceId)) {
                    predicates.add(criteriaBuilder.equal(root.get("serviceId"), serviceId));
                }

                if (!StringUtils.isEmpty(note)) {
                    predicates.add(criteriaBuilder.like(root.get("remark"), "%" + note + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "serviceId");

        Pageable pageable = PageRequest.of(start - 1, pageSize, sort);

        Page<PaasServiceInstance> page = paasServiceInstanceRepository.findAll(querySpecifi, pageable);

        return page;
    }

    @Override
    public PaasServiceInstance findByServiceId(String serviceId) {
        return paasServiceInstanceRepository.findByServiceId(serviceId);
    }
}
