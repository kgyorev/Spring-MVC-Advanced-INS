package com.insurance.ins.business.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by K on 6.4.2018 г..
 */
@Component
public class BusinessBatch {
    @Scheduled(cron = "0 0 1 ? * *")
    public void scheduleTaskUsingCronExpression() {

        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);
    }
}
