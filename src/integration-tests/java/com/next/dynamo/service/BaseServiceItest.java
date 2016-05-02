package com.next.dynamo.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ComponentScan(basePackages="com.next.dynamo")
@EnableJpaRepositories(basePackages="com.next.dynamo")
@Transactional
public class BaseServiceItest {

	public BaseServiceItest() {
		// TODO Auto-generated constructor stub
	}

}
