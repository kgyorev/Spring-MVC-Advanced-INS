package com.insurance.ins.business.batch;

import com.insurance.ins.financial.services.PremiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by K on 6.4.2018 г..
 */
@Component
public class BusinessBatch{

    private final PremiumService premiumService;

    @Autowired
    public BusinessBatch(PremiumService premiumService) {
        this.premiumService = premiumService;
    }


    @Scheduled(cron = "0 0 1 ? * *")
    public void scheduleTaskUsingCronExpression() {

        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);

       premiumService.premiumBillingBatch();

    }


    public void premiumBillingBatch() {
        premiumService.premiumBillingBatch();
    }
}
