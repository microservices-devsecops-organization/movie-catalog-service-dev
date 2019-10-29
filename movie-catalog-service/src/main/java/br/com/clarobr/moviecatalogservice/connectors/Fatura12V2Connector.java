package br.com.clarobr.moviecatalogservice.connectors;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservice.GlobalProperties;
import br.com.clarobr.moviecatalogservice.models.UserRating;

@Component(value = "fatura12V2Connector")
public class Fatura12V2Connector {

	@Autowired
    private GlobalProperties globalProperties;
	
	@Autowired
    private RestTemplate restTemplate;
	
	Logger logger = LoggerFactory.getLogger(Fatura12V2Connector.class);
	
	private HttpEntity<String> addHttpHeaderBasicAuth(String username, String password) {
		String plainCreds = username+":"+password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		return new HttpEntity<String>(headers);
	}
	
	public void requestFatura12V2() {
//		System.out.println("##### Requisitando SERVIÇO OSB LEGADO http://net-esb-legado-relacionar:6091/relacionar/atendimento/fatura/Fatura12V2");
		logger.info("globalProperties: " + " " + globalProperties.getUsername()+ " " + globalProperties.getPassword()+ " " 
				+ globalProperties.getDbHost());
		try {
			// Comentado pois no ambiente local não existe este serviço. 			
			restTemplate.exchange("http://net-esb-legado-relacionar:6091/relacionar/atendimento/fatura/Fatura12V2", HttpMethod.POST, 
				this.addHttpHeaderBasicAuth(globalProperties.getUsername(), globalProperties.getPassword()), UserRating.class);
		} catch (Throwable t) {
//			System.out.println("ERRO AO CHAMAR SERVIÇO OSB LEGADO");
			//t.printStackTrace();
//			System.out.println(t.getMessage());
			
			
			logger.error("Error: ", t);
			
			
//			StringWriter writer = new StringWriter();
//			PrintWriter printWriter= new PrintWriter(writer);
//			t.printStackTrace(printWriter);
//			
//			logger.error("Error: ", t.printStackTrace(new PrintWriter(new StringWriter())));
			
		}
	}
	
}
