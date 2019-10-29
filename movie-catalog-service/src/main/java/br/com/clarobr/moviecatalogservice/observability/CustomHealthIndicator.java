package br.com.clarobr.moviecatalogservice.observability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservice.models.Movie;
import br.com.clarobr.moviecatalogservice.models.UserRating;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private boolean isHealthy = false;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private int statusCodeRatingsDataService;
    
    private int statusCodeMovieInfoService;
    
    @Override
    public Health health() {
    	statusCodeRatingsDataService = restTemplate.getForEntity("http://ratings-data-service-cluster-ip-service:8083/actuator", UserRating.class).getStatusCodeValue();
    	statusCodeMovieInfoService = restTemplate.getForEntity("http://movie-info-service-cluster-ip-service:8082/actuator", Movie.class).getStatusCodeValue();
    	
    	if (statusCodeRatingsDataService == 200 && statusCodeMovieInfoService == 200) {
    		isHealthy = true;
    	}
    	return isHealthy ? Health.up().build() : Health.down().build();
    }
}
