package br.com.clarobr.moviecatalogservice.observability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservice.GlobalProperties;
import br.com.clarobr.moviecatalogservice.models.Movie;
import br.com.clarobr.moviecatalogservice.models.UserRating;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private boolean isHealthy = false;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GlobalProperties globalProperties;
    
    private int statusCodeRatingsDataService;
    
    private int statusCodeMovieInfoService;
    
    @Override
    public Health health() {
    	statusCodeRatingsDataService = restTemplate.getForEntity("http://"+globalProperties.getRatingsDataServiceHostname()+
				":"+globalProperties.getRatingsDataServicePort()+"/actuator", UserRating.class).getStatusCodeValue();
    	
    	statusCodeMovieInfoService = restTemplate.getForEntity("http://"+globalProperties.getMovieInfoServiceHostname()+
				":"+globalProperties.getMovieInfoServicePort()+"/actuator", Movie.class).getStatusCodeValue();
    	
    	if (statusCodeRatingsDataService == 200 && statusCodeMovieInfoService == 200) {
    		isHealthy = true;
    	}
    	return isHealthy ? Health.up().build() : Health.down().build();
    }
}
