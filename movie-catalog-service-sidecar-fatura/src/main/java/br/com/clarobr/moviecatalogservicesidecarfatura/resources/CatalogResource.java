package br.com.clarobr.moviecatalogservicesidecarfatura.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clarobr.moviecatalogservicesidecarfatura.GlobalProperties;
import br.com.clarobr.moviecatalogservicesidecarfatura.correlation.RequestCorrelation;
import br.com.clarobr.moviecatalogservicesidecarfatura.services.Fatura12V2Service;

@RestController
@RequestMapping("/catalog") // same value must be defined in k8s ingress
public class CatalogResource {
	
	private Logger logger = LoggerFactory.getLogger(CatalogResource.class);
	
	@Autowired
	private Fatura12V2Service fatura12V2Service;
    
    @Autowired
    private GlobalProperties globalProperties;
    
    @GetMapping("/{userId}")
	public String getCatalog(@PathVariable("userId") String userId, @RequestHeader HttpHeaders headers) {
    	//TESTE de chamada a serviço legado, utilzação de basic authentication
	  	//fatura12V2Service.requestFatura12V2();
	
    	RequestCorrelation.setHeaders(headers);
		
		headers.forEach((key, value) -> {
			logger.info(String.format("##### Header '%s' = %s", key, value));
	    });
		
		logger.info("globalProperties: " + " " + globalProperties.getUsername()+ " " + globalProperties.getPassword());
		
		fatura12V2Service.requestService(userId, RequestCorrelation.getCorrelationid());

		return "Requisição processada com sucesso!"; 
	}

}