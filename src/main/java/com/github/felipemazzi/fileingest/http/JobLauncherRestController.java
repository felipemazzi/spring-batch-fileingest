package com.github.felipemazzi.fileingest.http;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.felipemazzi.fileingest.job.JobExecutionService;

@RestController
@RequestMapping("/jobs")
public class JobLauncherRestController {

	private JobExecutionService service;
	
	public JobLauncherRestController(@Autowired JobExecutionService service) {
		this.service = service;
	}
	
	@PostMapping
	public String start() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		return service.run();
	}
}