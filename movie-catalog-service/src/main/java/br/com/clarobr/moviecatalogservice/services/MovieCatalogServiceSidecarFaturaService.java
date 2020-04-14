package br.com.clarobr.moviecatalogservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clarobr.moviecatalogservice.connectors.MovieCatalogServiceSidecarFaturaConnector;

@Service(value = "movieCatalogServiceSidecarFaturaService")
public class MovieCatalogServiceSidecarFaturaService implements BusinessService  {
	
	@Autowired
	private MovieCatalogServiceSidecarFaturaConnector movieCatalogServiceSidecarFaturaConnector;
	
	public void requestService(String userId, String correlationid) {
		movieCatalogServiceSidecarFaturaConnector.requestMovieCatalogServiceSidecarFatura(userId, correlationid);
	}

}
