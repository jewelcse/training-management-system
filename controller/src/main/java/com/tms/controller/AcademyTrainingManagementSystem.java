package com.tms.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tms.*"})
@EntityScan(basePackages = {"com.tms.*"})
@EnableJpaRepositories(basePackages = {"com.tms.*"})
public class AcademyTrainingManagementSystem {
    public static void main(String[] args) {
        SpringApplication.run(AcademyTrainingManagementSystem.class);
    }
}
