package com.numankaraaslan.spring_rbac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component(value = "beanfactory")
public class BeanFactory
{
	@Bean(name = "bCryptPasswordEncoder")
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}