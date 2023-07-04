package com.cspiration.mooc;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(value = "com/cspiration/mooc/dao")
@SpringBootApplication
public class CsonMoocApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsonMoocApplication.class, args);
    }

}
