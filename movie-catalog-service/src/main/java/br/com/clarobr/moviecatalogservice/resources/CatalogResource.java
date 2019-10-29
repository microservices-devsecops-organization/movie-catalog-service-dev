package br.com.clarobr.moviecatalogservice.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clarobr.moviecatalogservice.correlation.RequestCorrelation;
import br.com.clarobr.moviecatalogservice.services.Fatura12V2Service;
//import br.com.clarobr.moviecatalogservice.services.Fatura12V2Service;
import br.com.clarobr.moviecatalogservice.services.RatingsDataService;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
	
    @Autowired
    private RatingsDataService ratingsDataService;
    
    @Autowired
    private Fatura12V2Service fatura12V2Service;
    
    Logger logger = LoggerFactory.getLogger(CatalogResource.class);
    
    @RequestMapping("/{userId}")
	public String getCatalog(@PathVariable("userId") String userId,
			@RequestHeader HttpHeaders headers) {
		
		//TESTE de chamada a serviço legado, utilzação de basic authentication
	  	//fatura12V2Service.requestFatura12V2();
	
		RequestCorrelation.setHeaders(headers);
		
		ratingsDataService.requestService(userId);

		return "Requisição processada com sucesso!";
	}

}
