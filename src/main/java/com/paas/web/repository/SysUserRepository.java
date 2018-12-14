package com.paas.web.repository;

import com.paas.web.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser, Integer> {
    SysUser findByUsername(String username);
}
