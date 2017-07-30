package com.skart.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jndi.JndiObjectFactoryBean;

//@Configuration
public class EmbeddedTomcatWithSSLConfiguration {

	// http://docs.spring.io/spring-boot/docs/1.1.5.RELEASE/reference/htmlsingle/#howto-enable-multiple-connectors-in-tomcat

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory(){


			@Override
			protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatEmbeddedServletContainer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName("jdbc/skart");
				resource.setType(DataSource.class.getName());
				resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
				resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
				resource.setProperty("url", "jdbc:mysql://localhost:3306/skart");
				resource.setProperty("password", "");
				resource.setProperty("username", "root");

				context.getNamingResources().addResource(resource);
			}
		
		};
		tomcat.setContextPath(getContextPath());
		tomcat.addAdditionalTomcatConnectors(createSslConnector());
		return tomcat;
	}
	
	@Bean(destroyMethod = "")
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:comp/env/jdbc/skart");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}

	private String getContextPath() {
		return "/skart";
	}

	protected Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		try {
			File keystore = getFile(getKeystore());
			File truststore = keystore;
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(getHTTPSPort());
			protocol.setSSLEnabled(true);
			protocol.setKeystoreFile(keystore.getAbsolutePath());
			protocol.setKeystorePass(getKeystorePass());
			protocol.setTruststoreFile(truststore.getAbsolutePath());
			protocol.setTruststorePass(getKeystorePass());
			// ? protocol.setKeyAlias("apitester");
			return connector;
		} catch (IOException ex) {
			throw new IllegalStateException(
					"can't access keystore: [" + "keystore" + "] or truststore: [" + "keystore" + "]", ex);
		}
	}

	protected int getHTTPSPort() {
		// TODO This shouldn't be hard-coded here, but configurable
		return 8444;
	}

	protected String getKeystorePass() {
		return "openmf";
	}

	protected Resource getKeystore() {
		return new ClassPathResource("/keystore.jks");
	}

	public File getFile(Resource resource) throws IOException {
		try {
			return resource.getFile();
		} catch (IOException e) {
			// Uops.. OK, try again (below)
		}

		try {
			URL url = resource.getURL();
			/**
			 * // If this creates filenames that are too long on Win, // then
			 * could just use resource.getFilename(), // even though not unique,
			 * real risk prob. min.bon String tempDir =
			 * System.getProperty("java.io.tmpdir"); tempDir = tempDir + "/" +
			 * getClass().getSimpleName() + "/"; String path = url.getPath();
			 * String uniqName = path.replace("file:/", "").replace('!', '_');
			 * String tempFullPath = tempDir + uniqName;
			 **/
			// instead of File.createTempFile(prefix?, suffix?);
			File targetFile = new File(resource.getFilename());
			long len = resource.contentLength();
			if (!targetFile.exists() || targetFile.length() != len) { // Only
																		// copy
																		// new
																		// files
				FileUtils.copyURLToFile(url, targetFile);
			}
			return targetFile;
		} catch (IOException e) {
			// Uops.. erm, give up:
			throw new IOException("Cannot obtain a File for Resource: " + resource.toString(), e);
		}

	}
}
