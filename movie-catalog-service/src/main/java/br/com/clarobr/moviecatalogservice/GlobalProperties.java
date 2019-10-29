package br.com.clarobr.moviecatalogservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties // no prefix, root level.
public class GlobalProperties {
	
	// From k8s ConfigMap
	private String dbHost;
	
	// From k8s Secret 	
	private String username;
	
	// From k8s Secret
	private String password;
	
	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
