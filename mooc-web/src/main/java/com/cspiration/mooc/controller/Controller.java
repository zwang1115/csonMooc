package com.cspiration.mooc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {

    @RequestMapping("helloworld")
    public String helloworld() {
        log.info("Hello World 日志");
        return "helloworldOfCSON";
    }
}
