package com.starters.applyservice;

import com.starters.applyservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ApplicationService applicationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void afterSupply() {
        applicationService.setApplicationStatus();
    }
}
