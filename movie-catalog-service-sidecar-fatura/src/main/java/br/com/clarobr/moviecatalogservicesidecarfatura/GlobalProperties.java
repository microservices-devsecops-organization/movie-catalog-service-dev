package br.com.clarobr.moviecatalogservicesidecarfatura;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties // no prefix, root level.
public class GlobalProperties {
	
	// From k8s Secret 	
	private String username;
	
	// From k8s Secret
	private String password;
		
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
