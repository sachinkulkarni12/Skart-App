package com.skart.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skart.endpoint.ArticleEndpiont;

@Component
@ApplicationPath("/skart-app")
public class JerseyConfig extends ResourceConfig {
	
	@Autowired
	public JerseyConfig() {
		packages("com.skart");
		register(ArticleEndpiont.class);
	}

}
