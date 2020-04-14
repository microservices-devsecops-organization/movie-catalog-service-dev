package br.com.clarobr.moviecatalogservicesidecarfatura.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clarobr.moviecatalogservicesidecarfatura.connectors.Fatura12V2Connector;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Try;

@Service(value = "fatura12V2Service")
public class Fatura12V2Service implements BusinessService  {
	
	private Logger logger = LoggerFactory.getLogger(Fatura12V2Service.class);
	
	private String fatura;
	
	@Autowired
	private Fatura12V2Connector fatura12V2Connector; 
	
	private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("fatura12v2-service-circuitbreaker");
	
	public void requestService(String userId, String correlationid) {
		Observable<String> obs = Observable.<String>create(sub -> {
			fatura = this.requestServiceCircuitBreaker(userId, correlationid);
			sub.onNext(fatura);
			sub.onComplete();
		}).doOnNext(c -> logger.info("ratings-data-service were retrieved successfully."))
				.doOnError(e -> logger.error("An ERROR occurred while retrieving the ratings-data-service." + e));

		//síncrono
		//obs.subscribe();
		//assincrono
		obs.subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe((faturaReturned) -> this.handleResponseFatura(faturaReturned, correlationid), Throwable::printStackTrace);
		
	}
	
	public String requestServiceCircuitBreaker(String userId, String correlationid) {
		Supplier<String> backendFunction = CircuitBreaker.decorateSupplier(circuitBreaker, () -> fatura12V2Connector.requestFatura12V2(userId, correlationid));
		return Try.ofSupplier(backendFunction).recover(this::recovery).get();
	}
	
	private String recovery(Throwable throwable) {
		// Convertendo a Stack Trace em String para ser visualizada em um único registro de log no Kibana.
		StringWriter writer = new StringWriter();
		PrintWriter printWriter= new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		logger.error(writer.toString());

		// Handle exception and invoke fallback
		String faturaLocal = "Fatura Default";
		
        return faturaLocal;
    }
	
	public void handleResponseFatura(String fatura, String correlationId) {
		logger.info("Fatura: "+fatura+" with correlationId "+correlationId);
	}
	
}
