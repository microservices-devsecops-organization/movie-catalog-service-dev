package br.com.clarobr.moviecatalogservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties // no prefix, root level.
public class GlobalProperties {
	
	// From k8s ConfigMap
	private String dbHost;
	
	// From k8s ConfigMap
	private String ratingsDataServiceHostname;

	// From k8s ConfigMap
	private String ratingsDataServicePort;

	// From k8s ConfigMap
	private String movieInfoServiceHostname;

	// From k8s ConfigMap
	private String movieInfoServicePort;
	
	// From k8s ConfigMap
	private String movieCatalogServiceSidecarFaturaHostname;
	
	// From k8s ConfigMap
	private String movieCatalogServiceSidecarFaturaPort;
	
	public String getRatingsDataServiceHostname() {
		return ratingsDataServiceHostname;
	}

	public void setRatingsDataServiceHostname(String ratingsDataServiceHostname) {
		this.ratingsDataServiceHostname = ratingsDataServiceHostname;
	}

	public String getRatingsDataServicePort() {
		return ratingsDataServicePort;
	}

	public void setRatingsDataServicePort(String ratingsDataServicePort) {
		this.ratingsDataServicePort = ratingsDataServicePort;
	}

	public String getMovieInfoServiceHostname() {
		return movieInfoServiceHostname;
	}

	public void setMovieInfoServiceHostname(String movieInfoServiceHostname) {
		this.movieInfoServiceHostname = movieInfoServiceHostname;
	}

	public String getMovieInfoServicePort() {
		return movieInfoServicePort;
	}

	public void setMovieInfoServicePort(String movieInfoServicePort) {
		this.movieInfoServicePort = movieInfoServicePort;
	}
		
	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	
	public String getMovieCatalogServiceSidecarFaturaPort() {
		return movieCatalogServiceSidecarFaturaPort;
	}

	public void setMovieCatalogServiceSidecarFaturaPort(String movieCatalogServiceSidecarFaturaPort) {
		this.movieCatalogServiceSidecarFaturaPort = movieCatalogServiceSidecarFaturaPort;
	}
	
	public String getMovieCatalogServiceSidecarFaturaHostname() {
		return movieCatalogServiceSidecarFaturaHostname;
	}

	public void setMovieCatalogServiceSidecarFaturaHostname(String movieCatalogServiceSidecarFaturaHostname) {
		this.movieCatalogServiceSidecarFaturaHostname = movieCatalogServiceSidecarFaturaHostname;
	}
	
}
