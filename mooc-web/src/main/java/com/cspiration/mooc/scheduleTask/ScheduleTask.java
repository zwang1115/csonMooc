package com.cspiration.mooc.scheduleTask;

import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    //@Scheduled(cron = "*/1 * * * * ?")
    //@Scheduled(fixedDelay = 3000)
//    @Scheduled(initialDelay = 1000, fixedRate = 3000)
    public void sayHello() {
        System.out.println("cspiration hello");
    }
}
