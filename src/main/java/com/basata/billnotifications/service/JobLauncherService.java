package com.basata.billnotifications.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class JobLauncherService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job billJob;

    @Autowired
    PGNotificationService1 pgNotificationService;

    public void runJobWithRandomParameters() throws Exception {
        Random random = new Random();
        long randomValue = random.nextLong();

        Thread notificationThread = new Thread(pgNotificationService);
        notificationThread.start();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("randomValue", randomValue)
                .toJobParameters();

        jobLauncher.run(billJob, jobParameters);
    }
}
