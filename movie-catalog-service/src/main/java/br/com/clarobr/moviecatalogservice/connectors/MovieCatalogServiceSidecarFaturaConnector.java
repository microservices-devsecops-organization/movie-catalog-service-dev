package br.com.clarobr.moviecatalogservice.connectors;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservice.GlobalProperties;
import br.com.clarobr.moviecatalogservice.correlation.RequestCorrelation;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component(value = "movieCatalogServiceSidecarFaturaConnector")
public class MovieCatalogServiceSidecarFaturaConnector extends Connector {

	@Autowired
    private GlobalProperties globalProperties;
	
	@Autowired
    private RestTemplate restTemplate;
	
	public void requestMovieCatalogServiceSidecarFatura(String userId, String correlationid) {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(RequestCorrelation.CORRELATION_ID_HEADER, correlationid);
		
		restTemplate.exchange("http://"+globalProperties.getMovieCatalogServiceSidecarFaturaHostname()+":"
				+globalProperties.getMovieCatalogServiceSidecarFaturaPort()+"/movie-catalog-service-sidecar-fatura/catalog/" + userId, 
				HttpMethod.GET, this.addHttpHeaders(headers), String.class);
	}
	
}
