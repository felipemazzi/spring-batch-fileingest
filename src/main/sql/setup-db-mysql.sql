DROP DATABASE IF EXISTS db_springbatch;

DROP DATABASE IF EXISTS db_output;

DROP TABLE IF EXISTS db_output.person;

CREATE DATABASE db_springbatch;

CREATE DATABASE db_output;

CREATE TABLE db_output.person (
	id INT NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	phone_number BIGINT,
	email_address VARCHAR(255),
	PRIMARY KEY(id)
);
