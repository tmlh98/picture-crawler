package com.tx.pic.job;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 这里使用Quartz定时任务来处理定时关闭失效连接
 */
// 同步执行任务(单线程)
@DisallowConcurrentExecution  
public class CloseConnectJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CloseConnectJob.class);
	
	// 需要调度的任务
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 获取spring容器
		ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap()
				.get("context");

		// 从容器中获取HttpClient连接管理器
		PoolingHttpClientConnectionManager cm = applicationContext.getBean(PoolingHttpClientConnectionManager.class);

		// 关闭失效连接
		cm.closeExpiredConnections();
		
		JobKey key = context.getJobDetail().getKey();//获取JobDetail的标识信息
		LOGGER.info("关闭失效连接 , jobDetail : {} " , key.getName());
	}

}
