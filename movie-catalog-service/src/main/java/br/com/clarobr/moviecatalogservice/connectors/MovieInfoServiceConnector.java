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
import br.com.clarobr.moviecatalogservice.models.Movie;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@CircuitBreaker(name = "movie-info-service-circuitbreaker")
@Retry(name = "movie-info-service-retry")
@Bulkhead(name = "movie-info-service-bulkhead")
@Component(value = "movieInfoServiceConnector")
public class MovieInfoServiceConnector  extends Connector {
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private GlobalProperties globalProperties;

	public Movie requestMovieInfoService(String movieId, String correlationid) {
		if (!movieId.equalsIgnoreCase("exception")) {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(RequestCorrelation.CORRELATION_ID_HEADER, correlationid);
			
			ResponseEntity<Movie> movieresp = restTemplate.exchange("http://"+globalProperties.getMovieInfoServiceHostname()+
					":"+globalProperties.getMovieInfoServicePort()+"/movies/" + movieId, 
					HttpMethod.GET, this.addHttpHeaders(headers), Movie.class);
			Movie movie = movieresp.getBody();
			return movie;
		} else {
			// Simulando erro para testar retry e recovery
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception test for recovery and retry of circuit breaker!");
		}
	}
  
}
