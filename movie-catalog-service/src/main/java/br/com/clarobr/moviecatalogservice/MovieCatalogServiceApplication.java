package br.com.clarobr.moviecatalogservice;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import br.com.clarobr.moviecatalogservice.correlation.CorrelationHeaderFilter;

@SpringBootApplication
public class MovieCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
    public FilterRegistrationBean<CorrelationHeaderFilter> correlationHeaderFilter() {
        FilterRegistrationBean<CorrelationHeaderFilter> filterRegBean = new FilterRegistrationBean<CorrelationHeaderFilter>();
        filterRegBean.setFilter(new CorrelationHeaderFilter());
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegBean;
    }
}

