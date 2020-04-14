package br.com.clarobr.moviecatalogservice.correlation;

import org.springframework.http.HttpHeaders;

public class RequestCorrelation {

	public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

	private static String correlationId;

	private static final ThreadLocal<String> id = new ThreadLocal<String>();
	
	private static HttpHeaders headers = null;
	
	private RequestCorrelation() {
	    throw new IllegalStateException("Utility class");
	  }

    public static HttpHeaders getHeaders() {
		return headers;
	}

	public static void setHeaders(HttpHeaders headers) {
		RequestCorrelation.headers = headers;
	}

	public static String getCorrelationid() {
		return correlationId;
	}
    
    public static void setCorrelationid(String correlationid) {
		correlationId = correlationid;
	}

    public static void setId(String correlationId) {
        id.set(correlationId);
    }

    public static String getId() {
        return id.get();
    }
    
    public static void unloadId() {
    	id.remove(); 
      }
}