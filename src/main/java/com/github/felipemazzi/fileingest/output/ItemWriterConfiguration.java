package com.github.felipemazzi.fileingest.output;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemWriterConfiguration {

	@Bean
	public ItemWriter<PersonOutput> writer(@Qualifier("outputDataSource") DataSource outputDataSource) {
		return new JdbcBatchItemWriterBuilder<PersonOutput>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO person (first_name, last_name, phone_number, email_address) "
					+ "VALUES(:firstName, :lastName, :phoneNumber, :emailAddress)")
			.dataSource(outputDataSource)
			.build();
	}
}
