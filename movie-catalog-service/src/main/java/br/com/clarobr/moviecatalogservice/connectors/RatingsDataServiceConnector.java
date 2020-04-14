package br.com.clarobr.moviecatalogservice.connectors;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservice.GlobalProperties;
import br.com.clarobr.moviecatalogservice.correlation.RequestCorrelation;
import br.com.clarobr.moviecatalogservice.models.UserRating;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@CircuitBreaker(name = "ratings-data-service-circuitbreaker")
@Retry(name = "ratings-data-service-retry")
@Bulkhead(name = "ratings-data-service-bulkhead")
@Component(value = "ratingsDataServiceConnector")
public class RatingsDataServiceConnector extends Connector {
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private GlobalProperties globalProperties;
	
	public UserRating requestRatingsDataService(String userId, String correlationid) {
		if (!userId.equalsIgnoreCase("exception")) {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(RequestCorrelation.CORRELATION_ID_HEADER, correlationid);
			
			ResponseEntity<UserRating> userRatingresp = restTemplate.exchange("http://"+globalProperties.getRatingsDataServiceHostname()+
					":"+globalProperties.getRatingsDataServicePort()+"/ratingsdata/user/" + userId, 
					HttpMethod.GET, this.addHttpHeaders(headers), UserRating.class);
			UserRating userRating = userRatingresp.getBody();
			return userRating;
			
		} else {
			// Simulando erro para testar retry e recovery
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception test for recovery and retry of circuit breaker!");
		}
		
	}
	
}
