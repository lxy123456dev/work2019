package com.leyou.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置类 读取ly.cors 下所有信息
 */
@Data
@Component
@Configuration
@ConfigurationProperties("ly.cors")
public class CorsProperties {

    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private String filterPath;
    private Long maxAge;
    private Boolean allowedCredentials;

}
