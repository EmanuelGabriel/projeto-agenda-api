package br.com.srsolution.agenda.core.security;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfigCors {

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {

		CorsConfiguration corsConfig = new CorsConfiguration();
		var todosOrigins = Arrays.asList("*");
		corsConfig.setAllowedOrigins(todosOrigins);
		corsConfig.setAllowedHeaders(todosOrigins);
		corsConfig.setAllowedMethods(todosOrigins);
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		CorsFilter corsFilter = new CorsFilter(source);
		FilterRegistrationBean<CorsFilter> filter = new FilterRegistrationBean<>(corsFilter);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return filter;

	}
}
