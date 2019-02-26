package com.tx.pic.config;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.tx.pic.job.CloseConnectJob;
import com.tx.pic.job.CrawlerPicJob;

@Configuration
public class SchedledCfg {

	// 定义关闭无效连接任务
	@Bean("crawlerAutohomeJobBean")
	public JobDetailFactoryBean crawlerAutohomeJobBean() {
		JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
		jobDetailFactoryBean.setApplicationContextJobDataKey("context");
		jobDetailFactoryBean.setJobClass(CrawlerPicJob.class);
		jobDetailFactoryBean.setDurability(true);

		return jobDetailFactoryBean;
	}

	// 定义关闭无效连接触发器
	@Bean("crawlerAutohomeJobTrigger")
	public CronTriggerFactoryBean crawlerAutohomeJobTrigger(
			@Qualifier(value = "crawlerAutohomeJobBean") JobDetailFactoryBean itemJobBean) {
		CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
		tigger.setJobDetail(itemJobBean.getObject());
		tigger.setCronExpression("0/5 * * * * ? ");
		return tigger;
	}

	// 定义关闭无效连接任务
	@Bean("closeConnectJobBean")
	public JobDetailFactoryBean closeConnectJobBean() {
		JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
		jobDetailFactoryBean.setApplicationContextJobDataKey("context");
		jobDetailFactoryBean.setJobClass(CloseConnectJob.class);
		jobDetailFactoryBean.setDurability(true);

		return jobDetailFactoryBean;
	}

	// 定义关闭无效连接触发器
	@Bean("closeConnectJobTrigger")
	public CronTriggerFactoryBean closeConnectJobTrigger(
			@Qualifier(value = "closeConnectJobBean") JobDetailFactoryBean itemJobBean) {
		CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
		tigger.setJobDetail(itemJobBean.getObject());
		tigger.setCronExpression("0/5 * * * * ? ");
		return tigger;
	}

	// 定义调度器
	@Bean
	public SchedulerFactoryBean schedulerFactory(CronTrigger[] cronTriggerImpl) {
		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		bean.setTriggers(cronTriggerImpl);
		return bean;
	}

}
