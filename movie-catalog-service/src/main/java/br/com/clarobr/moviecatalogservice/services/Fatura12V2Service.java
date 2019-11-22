package br.com.clarobr.moviecatalogservice.services;

import org.springframework.stereotype.Service;

import br.com.clarobr.moviecatalogservice.connectors.Fatura12V2Connector;

@Service(value = "fatura12V2Service")
public class Fatura12V2Service implements BusinessService  {

	private Fatura12V2Connector fatura12V2Connector;
	
	public Fatura12V2Service(Fatura12V2Connector fatura12V2Connector) {
		this.fatura12V2Connector = fatura12V2Connector;
	}
	
	public void requestFatura12V2() {
		fatura12V2Connector.requestFatura12V2();
	}

}
