package br.com.clarobr.moviecatalogservice.correlation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.MDC;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * CorrelationHeaderFilter
 */
public class CorrelationHeaderFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationHeaderFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
//    	Do nothing. Will be implemented in the future
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String currentCorrId = httpServletRequest.getHeader(RequestCorrelation.CORRELATION_ID_HEADER);

        if (currentCorrId == null) {
            currentCorrId = UUID.randomUUID().toString();
            LOGGER.info("No correlationId found in Header. Generated : " + currentCorrId);
        } else {
//        	Securiy risk. A malicious user could send the currentCorrId (RequestCorrelation.CORRELATION_ID_HEADER) parameter 
//        	with the value: "Firefox) was authenticated successfully\r\n[INFO] User bbb (Internet Explorer".
//        	Manually sanitize the parameter.
//        	LOGGER.info("Found correlationId in Header : " + currentCorrId);
            LOGGER.info("Found correlationId in Header : " + currentCorrId.replaceAll("[\r\n]","") );
        }
        MDC.put("correlationId", currentCorrId);
        RequestCorrelation.setId(currentCorrId);
        RequestCorrelation.setCorrelationid(currentCorrId);

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    	MDC.remove("correlationId");
    }
    
//    private boolean currentRequestIsAsyncDispatcher(HttpServletRequest httpServletRequest) {
//        return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
//    }

}