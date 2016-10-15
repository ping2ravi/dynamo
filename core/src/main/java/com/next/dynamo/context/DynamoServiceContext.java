package com.next.dynamo.context;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.next.dynamo.util.DynamoAssert;

@Configuration
@ComponentScan(basePackages={"com.next.dynamo"})
@EnableJpaRepositories(basePackages = { "com.next.dynamo.persistance.repository" })
@EntityScan(basePackages = { "com.next.dynamo.persistance" })
public class DynamoServiceContext {

	@Bean
	public DynamoAssert dynamoAssert(){
		return new DynamoAssert();
	}

}
