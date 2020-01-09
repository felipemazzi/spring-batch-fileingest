package com.github.felipemazzi.fileingest.job;

import java.net.MalformedURLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

import com.github.felipemazzi.fileingest.input.PersonInput;
import com.github.felipemazzi.fileingest.output.PersonOutput;
import com.github.felipemazzi.fileingest.processing.PersonItemProcessor;

@Configuration
@EnableBatchProcessing
public class FileIngestBatchConfiguration{
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private String inputFileUrl;
	
	private int linesToSkip;
	
	public FileIngestBatchConfiguration(
			@Autowired JobBuilderFactory jobBuilderFactory, 
			@Autowired StepBuilderFactory stepBuilderFactory,
			@Value("${input.file_url}") String inputFilePath,
			@Value("${input.lines_to_skip}") String linesToSkip) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.inputFileUrl = inputFilePath;
		this.linesToSkip = Integer.parseInt(linesToSkip);
	}

	@Bean
	public FlatFileItemReader<PersonInput> reader() throws MalformedURLException {
		return new FlatFileItemReaderBuilder<PersonInput>()
			.name("personItemReader")
			.resource(new UrlResource(inputFileUrl))
			.delimited()
			.names(new String[] {"Name", "Phone Number", "Email Address"})
			.linesToSkip(linesToSkip)
			.fieldSetMapper(new BeanWrapperFieldSetMapper<PersonInput>() {{ 
				setTargetType(PersonInput.class);
			}})
			.build();
	}
	
	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<PersonOutput> writer(@Qualifier("outputDataSource") DataSource outputDataSource) {
		return new JdbcBatchItemWriterBuilder<PersonOutput>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO person (first_name, last_name, phone_number, email_address) "
					+ "VALUES(:firstName, :lastName, :phoneNumber, :emailAddress)")
			.dataSource(outputDataSource)
			.build();
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
	public Step step1(JdbcBatchItemWriter<PersonOutput> writer) throws MalformedURLException {
		return stepBuilderFactory.get("step1")
			.<PersonInput, PersonOutput> chunk(10)
			.reader(reader())
			.processor(processor())
			.writer(writer)
			.build();
	}
}
