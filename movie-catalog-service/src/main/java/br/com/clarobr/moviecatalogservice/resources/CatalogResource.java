package br.com.clarobr.moviecatalogservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clarobr.moviecatalogservice.GlobalProperties;
import br.com.clarobr.moviecatalogservice.correlation.RequestCorrelation;
import br.com.clarobr.moviecatalogservice.services.RatingsDataService;

@RestController
@RequestMapping("/catalog") // same value must be defined in k8s ingress
public class CatalogResource {
	
	private Logger logger = LoggerFactory.getLogger(CatalogResource.class);
	
    @Autowired
    private RatingsDataService ratingsDataService;
    
    @Autowired
    private GlobalProperties globalProperties;
    
    @GetMapping("/{userId}")
	public String getCatalog(@PathVariable("userId") String userId, @RequestHeader HttpHeaders headers) {
    	RequestCorrelation.setHeaders(headers);
		
		headers.forEach((key, value) -> {
			logger.info(String.format("##### Header '%s' = %s", key, value));
	    });
		
		ratingsDataService.requestService(userId, RequestCorrelation.getCorrelationid());

		return "Requisição processada com sucesso!";
	}

}