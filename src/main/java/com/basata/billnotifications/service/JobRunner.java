package com.basata.billnotifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    @Autowired
    private JobLauncherService jobLauncherService;

    @Override
    public void run(String... args) throws Exception {
        jobLauncherService.runJobWithRandomParameters();
    }
}
