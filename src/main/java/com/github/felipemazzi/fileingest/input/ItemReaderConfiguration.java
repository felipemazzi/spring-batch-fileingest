package com.github.felipemazzi.fileingest.input;

import java.net.MalformedURLException;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

@Configuration
public class ItemReaderConfiguration {

	private String inputFileUrl;
	private int linesToSkip;
	
	public ItemReaderConfiguration(
			@Value("${input.file_url}") String inputFileUrl, 
			@Value("${input.lines_to_skip}") String linesToSkip) {
		this.inputFileUrl = inputFileUrl;
		this.linesToSkip = Integer.parseInt(linesToSkip);
	}

	@Bean
	public ItemReader<PersonInput> reader() throws MalformedURLException {
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
	
}
