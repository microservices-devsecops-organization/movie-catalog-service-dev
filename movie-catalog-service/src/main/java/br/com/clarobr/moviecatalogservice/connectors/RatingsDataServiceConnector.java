package br.com.clarobr.moviecatalogservice.connectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
public class RatingsDataServiceConnector implements Connector {
	
	@Autowired
		private RestTemplate restTemplate;
	
	@Autowired
	private GlobalProperties globalProperties;

	private String userId;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public UserRating requestRatingsDataService() {
		if (!this.userId.equalsIgnoreCase("exception")) {
			ResponseEntity<UserRating> userRatingresp = restTemplate.exchange("http://"+globalProperties.getRatingsDataServiceHostname()+
			":"+globalProperties.getRatingsDataServicePort()+"/ratingsdata/user/" + this.userId, HttpMethod.GET, new HttpEntity<String>(RequestCorrelation.getHeaders()), UserRating.class);
			UserRating userRating = userRatingresp.getBody();
			return userRating;
			
		} else {
			// Simulando erro para testar retry e recovery
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception test for recovery and retry of circuit breaker!");
		}
	}
	
}
