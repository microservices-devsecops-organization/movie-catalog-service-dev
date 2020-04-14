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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.clarobr.moviecatalogservice.connectors.MovieInfoServiceConnector;
import br.com.clarobr.moviecatalogservice.models.CatalogItem;
import br.com.clarobr.moviecatalogservice.models.Movie;
import br.com.clarobr.moviecatalogservice.models.Rating;
import br.com.clarobr.moviecatalogservice.models.UserRating;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Try;

@Service(value = "movieInfoService")
public class MovieInfoService  implements BusinessService  {

	private Logger logger = LoggerFactory.getLogger(MovieInfoService.class);
	
	@Autowired
	private MovieInfoServiceConnector movieInfoServiceConnector; 
	
	@Autowired
	private MovieCatalogServiceSidecarFaturaService movieCatalogServiceSidecarFaturaService;
	
	private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("movie-info-service-circuitbreaker");
	
	private Movie movie;
		
	private List<CatalogItem> list;
	
	public List<CatalogItem> getList() {
		return new ArrayList<CatalogItem>(list);
	}

	public void setList(List<CatalogItem> list) {
		if (list != null) {
			List<CatalogItem> clone = new ArrayList<>(list); 
			this.list = clone;
		} else {
			this.list = null;
		}
	}
		
	// There are two key types to understand when working with Rx:
	// Observable represents any object that can get data from a data source and whose state may be of interest in a way that other objects may register an interest
	// An observer is any object that wishes to be notified when the state of another object changes
	public void requestService(UserRating userRating, String correlationid) {
		for (Rating item: userRating.getRatings()) {
			Observable<Movie> obs = Observable.<Movie>create(sub -> {
				movie = this.requestServiceCircuitBreaker(item.getMovieId(), correlationid);
				sub.onNext(movie);
				sub.onComplete();
			}).doOnNext(c -> logger.info("movie-info-service were retrieved successfully."))
					.doOnError(e -> logger.error("An ERROR occurred while retrieving the movie-info-service." + e));
			
			//síncrono
			//obs.subscribe();
			//assincrono
			obs.subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe((movieReturned) -> this.handleResponseMovie(movieReturned, userRating, correlationid), Throwable::printStackTrace);
		}
	}
		
	public Movie requestServiceCircuitBreaker(String movieId, String correlationid) {
		Supplier<Movie> backendFunction = CircuitBreaker.decorateSupplier(circuitBreaker, () -> movieInfoServiceConnector.requestMovieInfoService(movieId, correlationid) );
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
	
	public List<CatalogItem> handleResponseMovie(Movie movie, UserRating userRating, String correlationid) {
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
	      
	      movieCatalogServiceSidecarFaturaService.requestService(userRating.getUserId(), correlationid);
		
	      return this.getList();
	}
	
	public void clearList() {
		this.setList(null);
	}
		
}