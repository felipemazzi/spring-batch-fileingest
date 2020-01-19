package com.github.felipemazzi.fileingest.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.felipemazzi.fileingest.input.PersonInput;
import com.github.felipemazzi.fileingest.output.PersonOutput;
import com.github.felipemazzi.fileingest.processing.PersonItemProcessor;

@Configuration
@EnableBatchProcessing
public class JobConfiguration{
	
	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	
	public JobConfiguration(
			@Autowired JobBuilderFactory jobBuilderFactory, 
			@Autowired StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public Job importPersonJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importPersonJob")
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(step1)
			.end()
			.build();
	}
	
	@Bean
	public Step step1(
			@Autowired ItemReader<PersonInput> reader, 
			@Autowired ItemWriter<PersonOutput> writer) {
		return stepBuilderFactory.get("step1")
			.<PersonInput, PersonOutput> chunk(10)
			.reader(reader)
			.processor(processor())
			.writer(writer)
			.build();
	}
}
