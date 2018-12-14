package com.paas.web.security;

import com.paas.web.domain.SysUser;
import com.paas.web.service.ISysUserService;
import com.paas.web.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {
    public static Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("用户的用户名: {}", username);
//        SysUser user = sysUserService.findByUsername(username);
//        if (user != null) {
        SysUserDetails userDetails = new SysUserDetails();
        userDetails.setId(1);
        userDetails.setUsername("root");
        userDetails.setPassword(MD5Util.encode("123456"));

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails.setAuthorities(list);

        return userDetails;
//        } else {
//            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
//        }
    }
}
