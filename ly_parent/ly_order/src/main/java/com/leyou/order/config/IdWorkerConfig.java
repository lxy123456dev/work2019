package com.leyou.order.config;


import com.leyou.order.properties.IdWorkerProperties;
import com.leyou.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdWorkerConfig {
    @Autowired
    private IdWorkerProperties prop;

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(prop.getWorkerId(), prop.getDataCenterId());
    }
}
