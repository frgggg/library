package com.cft.focusstart.library.quartz.finddebtors;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindDebtorsConfig {
    @Value("${time-for-find-debtors-circle}")
    private String findDebtorsCircleConfig;

    @Bean
    public JobDetail findDebtorsJobDetail(){
        return JobBuilder.newJob(FindDebtorsJob.class)
                .withIdentity(FindDebtorsJob.class.getSimpleName() + "-JobDetail-Identity")
                .storeDurably().build();
    }

    @Bean
    public Trigger findDebtorsJobTrigger(JobDetail findDebtorsJobDetail)
    {
        return TriggerBuilder.newTrigger().forJob(findDebtorsJobDetail)
                .withIdentity(FindDebtorsJob.class.getSimpleName() + "-Trigger-Identity")
                .withSchedule(CronScheduleBuilder.cronSchedule(findDebtorsCircleConfig))
                .build();
    }
}
