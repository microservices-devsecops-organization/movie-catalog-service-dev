package br.com.clarobr.moviecatalogservice.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clarobr.moviecatalogservice.connectors.RatingsDataServiceConnector;
import br.com.clarobr.moviecatalogservice.models.UserRating;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Try;

@Service(value = "ratingsDataService")
public class RatingsDataService implements BusinessService  {
	
	private Logger logger = LoggerFactory.getLogger(RatingsDataService.class);
	
	private final RatingsDataServiceConnector ratingsDataServiceConnector; 
	
	private final CircuitBreaker circuitBreaker;
	
	private UserRating userRating;

	@Autowired
	private MovieInfoService movieInfoService;
		
	public RatingsDataService(RatingsDataServiceConnector ratingsDataServiceConnector, 
			CircuitBreakerRegistry circuitBreakerRegistry, CircuitBreakerProperties circuitBreakerProperties) {
		this.ratingsDataServiceConnector = ratingsDataServiceConnector;
		this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("ratings-data-service-circuitbreaker", 
				() -> circuitBreakerProperties.createCircuitBreakerConfig("ratings-data-service-circuitbreaker"));
	}
	
	// There are two key types to understand when working with Rx:
	// Observable represents any object that can get data from a data source and whose state may be of interest in a way that other objects may register an interest
	// An observer is any object that wishes to be notified when the state of another object changes
	public void requestService(String userId) {
		movieInfoService.clearList();
		
		Observable<UserRating> obs = Observable.<UserRating>create(sub -> {
			ratingsDataServiceConnector.setUserId(userId);
			userRating = this.requestServiceCircuitBreaker();
			sub.onNext(userRating);
			sub.onComplete();
		}).doOnNext(c -> logger.info("ratings-data-service were retrieved successfully."))
				.doOnError(e -> logger.error("An ERROR occurred while retrieving the ratings-data-service." + e));

	  		
		//síncrono
		//obs.subscribe();
		//assincrono
		obs.subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe(this::handleResponseUserRating, Throwable::printStackTrace);
	}
	
	public UserRating requestServiceCircuitBreaker() {
		Supplier<UserRating> backendFunction = CircuitBreaker.decorateSupplier(circuitBreaker, ratingsDataServiceConnector::requestRatingsDataService);
		return Try.ofSupplier(backendFunction).recover(this::recovery).get();
	}
	
	private UserRating recovery(Throwable throwable) {
		// Convertendo a Stack Trace em String para ser visualizada em um único registro de log no Kibana.
		StringWriter writer = new StringWriter();
		PrintWriter printWriter= new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		logger.error(writer.toString());

		// Handle exception and invoke fallback
		UserRating userRatingLocal = new UserRating();
		userRatingLocal.initData("default");

        return userRatingLocal;
    }
	
	public void handleResponseUserRating(UserRating userRating) {
		movieInfoService.requestService(userRating);
	}
	

}