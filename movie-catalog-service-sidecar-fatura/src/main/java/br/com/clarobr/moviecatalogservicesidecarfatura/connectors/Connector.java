package br.com.clarobr.moviecatalogservicesidecarfatura.connectors;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public abstract class Connector {
	
	public HttpEntity<String> addHttpHeaders(HashMap<String, String> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();
		headers.forEach((key, value) -> {
			httpHeaders.add(key, value);
	    });
		return new HttpEntity<String>(httpHeaders);
	}
	 
}