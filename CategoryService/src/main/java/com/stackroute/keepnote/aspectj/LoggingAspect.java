package com.stackroute.keepnote.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.mongodb.core.mapping.Document;

/* Annotate this class with @Aspect and @Component */
@Aspect
@Document
public class LoggingAspect {
	/*
	 * Write loggers for each of the methods of Category controller, any particular
	 * method will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
}
