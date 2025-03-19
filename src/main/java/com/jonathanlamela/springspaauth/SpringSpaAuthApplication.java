package com.jonathanlamela.springspaauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.jonathanlamela.springspaauth.service.UserService;

@SpringBootApplication
public class SpringSpaAuthApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext cc = SpringApplication.run(SpringSpaAuthApplication.class, args);

		UserService us = cc.getBean("userSComponent", UserService.class);

		System.out.println(us.getServiceVersion());
	}

}
