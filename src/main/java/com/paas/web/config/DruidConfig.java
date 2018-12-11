package com.paas.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {
    public static Logger LOGGER = LoggerFactory.getLogger(DruidConfig.class);

    @Autowired
    private Environment env;

    //配置新的数据源
    @Bean
    public DataSource dataSource() {
        LOGGER.info("spring.datasource.url：{}", env.getProperty("spring.datasource.url"));
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        return dataSource;
    }

}
