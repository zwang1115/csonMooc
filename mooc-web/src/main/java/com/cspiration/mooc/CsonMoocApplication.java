package com.cspiration.mooc;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(value = "com/cspiration/mooc/dao")
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableHystrix
public class CsonMoocApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsonMoocApplication.class, args);
    }

}
