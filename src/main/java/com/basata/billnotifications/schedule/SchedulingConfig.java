//package com.basata.billnotifications.schedule;
//
//import com.basata.billnotifications.service.PGNotificationService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@EnableScheduling
//@Configuration
//@Slf4j
//public class SchedulingConfig {
//
//    private final JobLauncher jobLauncher;
//
//    private final Job job;
//
//    private final PGNotificationService pgNotificationService;
//
//    public SchedulingConfig(JobLauncher jobLauncher, Job job, PGNotificationService pgNotificationService) {
//        this.jobLauncher = jobLauncher;
//        this.job = job;
//        this.pgNotificationService = pgNotificationService;
//    }
//
//    @Scheduled(cron = "0 9 25-31 * *")
//    public void runJob() {
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String formattedDate = today.format(formatter);
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("time", formattedDate)
//                .toJobParameters();
//        try {
//            pgNotificationService.subscribe();
//            jobLauncher.run(job, jobParameters);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//}
