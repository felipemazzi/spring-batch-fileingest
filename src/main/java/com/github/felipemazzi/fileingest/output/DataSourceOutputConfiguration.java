package com.github.felipemazzi.fileingest.output;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceOutputConfiguration {

	@Bean
	@ConfigurationProperties("app.datasource.output")
	public DataSourceProperties outputDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	@ConfigurationProperties("app.datasource.output.configuration")
	public HikariDataSource outputDataSource() {
		return outputDataSourceProperties()
				.initializeDataSourceBuilder()
				.type(HikariDataSource.class)
				.build();
	}
}
