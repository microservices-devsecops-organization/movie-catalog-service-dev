package br.com.clarobr.moviecatalogservicesidecarfatura.observability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomHealthIndicator implements HealthIndicator {
	
    private boolean isHealthy = false;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private int statusCodeFaturaService;
    
    @Override
    public Health health() {
    	//using k8s endpoint service
    	// statusCodeFaturaService = restTemplate.getForEntity("http://net-esb-legado-relacionar:6091/relacionar/atendimento/fatura/Fatura12V2?WSDL", String.class).getStatusCodeValue();
        
        // A chamada acima foi comentada pois estava ocorrendo timeout ao realizar requisição no legado. Este erro é ocasionado devido a instabilidade do ambiente legado utilizado. Para casos reais a requisição ao serviço externa deve ser configurada!
        statusCodeFaturaService = 200;
    	if (statusCodeFaturaService == 200) {
    		isHealthy = true;
    	}
    	return isHealthy ? Health.up().build() : Health.down().build();
    }
}