package com.easyapper.eventsmicroservice.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.utility.EALogger;

@Component
@Aspect
public class CategoryServiceAspect {
	@Autowired
	EALogger logger;
	
	@Before(value="execution(* com.easyapper.eventsmicroservice.service.CategoryService.*(..))")
	public void beforeAdvice() {
		logger.info("Before: CategoryService");
	}
	
	@After("execution(* com.easyapper.eventsmicroservice.service.CategoryService.*(..))")
	public void afterAdvice() {
		logger.info("After : CategoryService");
	}
	
	@AfterThrowing("execution(* com.easyapper.eventsmicroservice.service.CategoryService.*(..))")
	public void afterThrowingAdvice() {
		logger.warning("After Throwing : CategoryService");
	}
}
