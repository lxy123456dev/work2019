package com.leyou;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LySmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySmsApplication.class, args
        );
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}

