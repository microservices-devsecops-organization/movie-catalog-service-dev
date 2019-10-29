package br.com.clarobr.moviecatalogservice.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.clarobr.moviecatalogservice.connectors.MovieInfoServiceConnector;
import br.com.clarobr.moviecatalogservice.models.CatalogItem;
import br.com.clarobr.moviecatalogservice.models.Movie;
import br.com.clarobr.moviecatalogservice.models.Rating;
import br.com.clarobr.moviecatalogservice.models.UserRating;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Try;

@Service(value = "movieInfoService")
public class MovieInfoService  implements BusinessService  {

	Logger logger = LoggerFactory.getLogger(MovieInfoService.class);
	
	private final MovieInfoServiceConnector movieInfoServiceConnector; 
	
	private final CircuitBreaker circuitBreaker;
	
	private Movie movie;
	
	private UserRating userRating;
		
	private List<CatalogItem> list;
	
	public List<CatalogItem> getList() {
		return list;
	}

	public void setList(List<CatalogItem> list) {
		this.list = list;
	}
		
	public MovieInfoService(MovieInfoServiceConnector movieInfoServiceConnector, 
			CircuitBreakerRegistry circuitBreakerRegistry, CircuitBreakerProperties circuitBreakerProperties) {
		this.movieInfoServiceConnector = movieInfoServiceConnector;
		this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("movie-info-service-circuitbreaker", 
				() -> circuitBreakerProperties.createCircuitBreakerConfig("movie-info-service-circuitbreaker"));
	}
	
	// There are two key types to understand when working with Rx:
	// Observable represents any object that can get data from a data source and whose state may be of interest in a way that other objects may register an interest
	// An observer is any object that wishes to be notified when the state of another object changes
	public void requestService(UserRating userRating) {
	  	this.userRating = userRating;
	  	
		for (Rating item: userRating.getRatings()) {
			movieInfoServiceConnector.setMovieId(item.getMovieId());
			Observable<Movie> obs = Observable.<Movie>create(sub -> {
				movie = this.requestServiceCircuitBreaker();
				sub.onNext(movie);
				sub.onComplete();
			}).doOnNext(c -> logger.info("movie-info-service were retrieved successfully."))
					.doOnError(e -> logger.error("An ERROR occurred while retrieving the movie-info-service." + e));
			
			//síncrono
			//obs.subscribe();
			//assincrono
			obs.subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe(this::handleResponseMovie, Throwable::printStackTrace);
		}
	}
		
	public Movie requestServiceCircuitBreaker() {
		Supplier<Movie> backendFunction = CircuitBreaker.decorateSupplier(circuitBreaker, movieInfoServiceConnector::requestMovieInfoService);
		return Try.ofSupplier(backendFunction).recover(this::recovery).get();
	}
	
	private Movie recovery(Throwable throwable) {
		// Convertendo a Stack Trace em String para ser visualizada em um único registro de log no Kibana. 
		StringWriter writer = new StringWriter();
		PrintWriter printWriter= new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		logger.error(writer.toString());
		
		return new Movie("default", "Name for movie ID " + "default");
    }
	
	public List<CatalogItem> handleResponseMovie(Movie movie) {
		// Descobrindo o indice do rating do movie informado
	      int position = IntStream.range(0, userRating.getRatings().size())
			.filter(i -> movie.getMovieId().equals(userRating.getRatings().get(i).getMovieId()))
			.findFirst()
			.orElse(-1);	
	      
	      CatalogItem item = new CatalogItem(movie.getName().concat(" and user "+ userRating.getUserId()), 
	    		  "Description", userRating.getRatings().get(position).getRating());
	      
	      if (this.list == null || this.list.isEmpty()) {
	    	  this.list = new ArrayList<CatalogItem>(Arrays.asList(item));
	      } else {
	    	  this.list.add(item);
	      }

	      Gson gson = new Gson();
	      logger.info(gson.toJson(this.list));
		
	      return this.list;
	}
	
	public void clearList() {
		this.setList(null);
	}
		
}
