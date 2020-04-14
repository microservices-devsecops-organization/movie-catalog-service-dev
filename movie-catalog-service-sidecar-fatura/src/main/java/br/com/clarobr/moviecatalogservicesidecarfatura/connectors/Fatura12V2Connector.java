package br.com.clarobr.moviecatalogservicesidecarfatura.connectors;

import java.util.HashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservicesidecarfatura.GlobalProperties;
import br.com.clarobr.moviecatalogservicesidecarfatura.correlation.RequestCorrelation;
import br.com.clarobr.moviecatalogservicesidecarfatura.services.Fatura12V2Service;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@CircuitBreaker(name = "fatura12v2-service-circuitbreaker")
@Retry(name = "fatura12v2-service-retry")
@Bulkhead(name = "fatura12v2-service-bulkhead")
@Component(value = "fatura12V2Connector")
public class Fatura12V2Connector extends Connector {

	@Autowired
    private GlobalProperties globalProperties;
	
	@Autowired
    private RestTemplate restTemplate;
	
	private Logger logger = LoggerFactory.getLogger(Fatura12V2Connector.class);
	
	public String requestFatura12V2(String userId, String correlationid) {
//		System.out.println("##### Requisitando SERVIÇO OSB LEGADO http://net-esb-legado-relacionar:6091/relacionar/atendimento/fatura/Fatura12V2");
		logger.info("userId: "+userId);
		byte[] plainCredsBytes = (globalProperties.getUsername() + ":" + globalProperties.getPassword()).getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic " + base64Creds);
		headers.put(RequestCorrelation.CORRELATION_ID_HEADER, correlationid);

		//using k8s endpoint service
		ResponseEntity<String> fatura = restTemplate.exchange("http://net-esb-legado-relacionar:6091/relacionar/atendimento/fatura/Fatura12V2?WSDL", HttpMethod.GET, 
			this.addHttpHeaders(headers), String.class);
		return fatura.getBody();
		
	}
	
}
