package com.ivt.mis.common;

/**
 *
 * @author edw
 */
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringAppContext {

	private Logger logger = Logger.getLogger(SpringAppContext.class);
	private ApplicationContext applicationContext;
	private static final SpringAppContext provider = new SpringAppContext();

	private SpringAppContext() throws ExceptionInInitializerError {
		try {
			this.applicationContext = new ClassPathXmlApplicationContext(
					"/config/spring.xml");
		} catch (Throwable ex) {
			logger.error(
					"Initial ApplicationContext creation failed -- "
							+ ex.getMessage(), ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public synchronized static SpringAppContext getInstance()
			throws ExceptionInInitializerError {
		return provider;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}