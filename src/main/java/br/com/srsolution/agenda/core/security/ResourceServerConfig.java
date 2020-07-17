package br.com.srsolution.agenda.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import br.com.srsolution.agenda.core.security.util.AuthExceptionEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private AuthExceptionEntryPoint authExceptionEntryPoint;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		// Configurações relativas ao Resources
		http
			.authorizeRequests()
				.antMatchers("/agenda/v1/clientes/**", "/agenda/v1/contatos/**").hasRole("USER")
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(authExceptionEntryPoint);	
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}
	
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}

}
