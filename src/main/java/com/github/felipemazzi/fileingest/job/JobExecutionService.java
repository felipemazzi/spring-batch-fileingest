package com.github.felipemazzi.fileingest.job;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobExecutionService {

	private JobLauncher jobLauncher;
	
	private Job job;
	
	private JobExplorer jobExplorer;
	
	public JobExecutionService(@Autowired JobLauncher jobLauncher, @Autowired Job job, 
			@Autowired JobExplorer jobExplorer) {
		
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.jobExplorer = jobExplorer;
	}

	public String run() {
		JobInstance lastJobInstance = jobExplorer.getLastJobInstance(job.getName());

		JobExecution lastJobExecution = null;
		JobParameters nextExecutionJobParameters = null;
		
		if (lastJobInstance != null) {
			lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);

			nextExecutionJobParameters = 
					job.getJobParametersIncrementer().getNext(lastJobExecution.getJobParameters());
		} else {
			nextExecutionJobParameters = new JobParametersBuilder().toJobParameters();
		}
		
		String returnMessage = "";
		
		if (lastJobExecution == null 
				|| lastJobExecution.getStatus().equals(BatchStatus.COMPLETED) 
				|| lastJobExecution.getStatus().equals(BatchStatus.FAILED)) {
			
			try {
				jobLauncher.run(job, nextExecutionJobParameters);
				returnMessage = "Started execution of job " + job.getName() + ".";
			} catch (JobExecutionAlreadyRunningException 
					| JobRestartException 
					| JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				returnMessage = "Failed to start " + job.getName() + ": " + e.getMessage();
			}
			
		} else {
			returnMessage = "Could not start " + job.getName() + ".";
			
			if (lastJobExecution != null) { 
				returnMessage += "Last execution status: " + lastJobExecution.getStatus() + ".";
			}
		}
		
		return returnMessage;
	}
}