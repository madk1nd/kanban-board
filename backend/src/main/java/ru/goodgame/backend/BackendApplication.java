package ru.goodgame.backend;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@SpringBootApplication
public class BackendApplication {

	@Autowired
	public DataSource dataSource;

	@Bean
	public Configuration getConfiguration() {
		SQLTemplates templates = new PostgreSQLTemplates();
		return new Configuration(templates);
	}

	@Bean
	public SQLQueryFactory getQueryFactory() {
		return new SQLQueryFactory(getConfiguration(), dataSource);
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
