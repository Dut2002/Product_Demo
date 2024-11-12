package com.example.demo_oracle_db.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {

    @Bean
    @Qualifier("jwtConfigAuth")
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

//    @Bean
//    public Gson gson() {
//        return new Gson();
//    }

}
