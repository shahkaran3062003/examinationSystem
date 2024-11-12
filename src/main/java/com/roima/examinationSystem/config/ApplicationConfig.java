package com.roima.examinationSystem.config;

import jakarta.ws.rs.core.Application;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ApplicationConfig extends Application {



    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
