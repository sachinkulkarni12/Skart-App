package com.skart.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestApiResource {
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public String getString(){
		return "welcome to SPRING WORLD";
	}
	
	@GET
	@Path("/private")
	@Consumes(MediaType.APPLICATION_JSON)
	public String getPrivateString(){
		return "welcome to SPRING WORLD Private";
	}

}
