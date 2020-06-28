package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-17
 * Time: 18:29
 */
@ComponentScan(basePackages = {"com.example.dao","com.example.controller"})
@SpringBootApplication()
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MainSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainSpringBootApplication.class);
    }
}
