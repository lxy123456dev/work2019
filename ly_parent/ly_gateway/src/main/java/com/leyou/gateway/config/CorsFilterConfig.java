package com.leyou.gateway.config;


import com.leyou.properties.CorsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsFilterConfig {


    @Autowired
    private CorsProperties prop;
    @Bean
    public CorsFilter corsFilter(){
        //配置Cors规则： 在响应头中增加允许跨域规则
        CorsConfiguration configuration = new CorsConfiguration();
        //1、设置允许哪些域名访问
        prop.getAllowedOrigins().stream().forEach(configuration::addAllowedOrigin);

        //2、设置允许哪些HTTP请求类型访问
        prop.getAllowedMethods().stream().forEach(configuration::addAllowedMethod);
//        configuration.addAllowedMethod("GET");
//        configuration.addAllowedMethod("POST");
//        configuration.addAllowedMethod("DELETE");
//        configuration.addAllowedMethod("PUT");
//        configuration.addAllowedMethod("OPTIONS");

        //3、设置允许哪些请求头访问
//        configuration.addAllowedHeader("*");
        prop.getAllowedHeaders().stream().forEach(configuration::addAllowedHeader);

        //4、设置预检请求有效期  Options请求检查服务器是否允许访问
        configuration.setMaxAge(prop.getMaxAge());

        //5、设置是否允许操作Cookie
        configuration.setAllowCredentials(prop.getAllowedCredentials());


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //设置过滤器拦截请求路径
        source.registerCorsConfiguration(prop.getFilterPath(), configuration);
        CorsFilter corsFilter = new CorsFilter(source);
        return  corsFilter;
    }

}
