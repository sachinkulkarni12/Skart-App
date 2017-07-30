package com.skart.config;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

import com.skart.filter.CORSFilter;

//@Configuration
public class WebXmlConfiguration {

	@Bean
	public Filter springSecurityFilterChain() {
		return new DelegatingFilterProxy();
	}

	@Bean
	public ServletRegistrationBean jersey() {
		Servlet jerseyServlet = new SpringServlet();
		ServletRegistrationBean jerseyServletRegistration = new ServletRegistrationBean();
		jerseyServletRegistration.setServlet(jerseyServlet);
		jerseyServletRegistration.addUrlMappings("/api/v1/*");
		jerseyServletRegistration.setName("jersey-servlet");
		jerseyServletRegistration.setLoadOnStartup(1);
		jerseyServletRegistration.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		jerseyServletRegistration.addInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters",
				CORSFilter.class.getName());
		jerseyServletRegistration.addInitParameter("com.sun.jersey.config.feature.DisableWADL", "true");
		// debugging for development:
		// jerseyServletRegistration.addInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters",
		// LoggingFilter.class.getName());
		return jerseyServletRegistration;
	}

}
