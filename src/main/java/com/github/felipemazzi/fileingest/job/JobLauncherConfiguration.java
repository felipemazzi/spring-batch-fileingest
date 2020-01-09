package com.github.felipemazzi.fileingest.job;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class JobLauncherConfiguration {
	
	private JobRepository jobRepository;
	
	public JobLauncherConfiguration(@Autowired JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Bean
	@Primary
	public JobLauncher asyncJobLauncher() {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		launcher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return launcher;
	}
}
