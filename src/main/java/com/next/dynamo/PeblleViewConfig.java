package com.next.dynamo;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring4.PebbleViewResolver;
import com.mitchellbosecke.pebble.spring4.extension.SpringExtension;

@Configuration
public class PeblleViewConfig {

	@Autowired
	private ServletContext servletContext;

	@Bean
	public Loader templateLoader() {
		return new ServletLoader(servletContext);
	}

	@Bean
	public SpringExtension springExtension() {
		return new SpringExtension();
	}

	@Bean
	public PebbleEngine pebbleEngine() {
		return new PebbleEngine.Builder().loader(this.templateLoader()).extension(springExtension()).build();
	}

	@Bean
	public ViewResolver viewResolver() {
		PebbleViewResolver viewResolver = new PebbleViewResolver();
		viewResolver.setPrefix("/WEB-INF/pebble/");
		viewResolver.setSuffix(".html");
		viewResolver.setPebbleEngine(pebbleEngine());
		return viewResolver;
	}

}
