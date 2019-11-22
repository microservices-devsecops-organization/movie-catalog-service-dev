package br.com.clarobr.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.clarobr.moviecatalogservice.GlobalProperties;
import br.com.clarobr.moviecatalogservice.correlation.RequestCorrelation;
import br.com.clarobr.moviecatalogservice.services.Fatura12V2Service;
import br.com.clarobr.moviecatalogservice.services.MovieInfoService;
//import br.com.clarobr.moviecatalogservice.services.Fatura12V2Service;
import br.com.clarobr.moviecatalogservice.services.RatingsDataService;

@RestController
@RequestMapping("/catalog") // same value must be defined in k8s ingress
public class CatalogResource {
	
	private Logger logger = LoggerFactory.getLogger(MovieInfoService.class);
	
    @Autowired
    private RatingsDataService ratingsDataService;
    
    @Autowired
    private Fatura12V2Service fatura12V2Service;
    
    @Autowired
    private GlobalProperties globalProperties;
    
    @GetMapping("/{userId}")
	public String getCatalog(@PathVariable("userId") String userId,
			@RequestHeader HttpHeaders headers) {
    	//TESTE de chamada a serviço legado, utilzação de basic authentication
	  	//fatura12V2Service.requestFatura12V2();
	
		RequestCorrelation.setHeaders(headers);
		
		logger.info("globalProperties: " + " " + globalProperties.getUsername()+ " " + globalProperties.getPassword()+ " " + globalProperties.getDbHost());

		ratingsDataService.requestService(userId);

		return "Requisição processada com sucesso!";
	}

}
